package com.buddyquest.service;

import com.buddyquest.dto.AuthResponse;
import com.buddyquest.dto.LoginRequest;
import com.buddyquest.dto.RegisterRequest;
import com.buddyquest.dto.UserSummaryResponse;
import com.buddyquest.model.Interest;
import com.buddyquest.model.PointLedger;
import com.buddyquest.model.Profile;
import com.buddyquest.model.User;
import com.buddyquest.repository.PointLedgerRepository;
import com.buddyquest.repository.ProfileRepository;
import com.buddyquest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PointLedgerRepository pointLedgerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PointLedgerRepository pointLedgerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.pointLedgerRepository = pointLedgerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }

        String passwordHash = passwordEncoder.encode(req.password());

        User user = new User(req.email(), req.displayName(), passwordHash);
        user = userRepository.save(user);

        Profile profile = new Profile(user.getId(), req.bio());
        profileRepository.save(profile);

        PointLedger ledger = new PointLedger(user.getId());
        pointLedgerRepository.save(ledger);

        UserSummaryResponse summary = new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                profile.getBio(),
                ledger.getPoints(),
                List.of()
        );

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, summary);
    }

    public AuthResponse login(LoginRequest req) {
        // бросит исключение, если пароль неверный
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.email(),
                        req.password()
                )
        );

        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String bio = profileRepository.findByUserId(user.getId())
                .map(Profile::getBio)
                .orElse("");

        int points = pointLedgerRepository.findById(user.getId())
                .map(PointLedger::getPoints)
                .orElse(0);

        List<String> interests = user.getInterests().stream()
                .map(Interest::getName)
                .toList();

        UserSummaryResponse summary = new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                bio,
                points,
                interests
        );

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, summary);
    }
}
