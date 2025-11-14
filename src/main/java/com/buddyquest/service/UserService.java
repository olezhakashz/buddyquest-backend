package com.buddyquest.service;

import com.buddyquest.dto.CreateUserRequest;
import com.buddyquest.dto.UserSummaryResponse;
import com.buddyquest.model.PointLedger;
import com.buddyquest.model.Profile;
import com.buddyquest.model.User;
import com.buddyquest.repository.PointLedgerRepository;
import com.buddyquest.repository.ProfileRepository;
import com.buddyquest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PointLedgerRepository pointLedgerRepository;

    public UserService(UserRepository userRepository,
                       ProfileRepository profileRepository,
                       PointLedgerRepository pointLedgerRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.pointLedgerRepository = pointLedgerRepository;
    }

    @Transactional
    public UserSummaryResponse createUser(CreateUserRequest req) {
        User user = new User(req.email(), req.displayName());
        user = userRepository.save(user);

        Profile profile = new Profile(user.getId(), req.bio());
        profileRepository.save(profile);

        PointLedger ledger = new PointLedger(user.getId());
        pointLedgerRepository.save(ledger);

        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                profile.getBio(),
                ledger.getPoints()
        );
    }

    public UserSummaryResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String bio = profileRepository.findByUserId(id)
                .map(Profile::getBio)
                .orElse("");

        int points = pointLedgerRepository.findById(id)
                .map(PointLedger::getPoints)
                .orElse(0);

        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                bio,
                points
        );
    }
}
