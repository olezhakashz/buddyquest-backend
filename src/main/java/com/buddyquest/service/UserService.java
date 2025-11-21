package com.buddyquest.service;

import java.util.List;

import com.buddyquest.dto.LoginRequest;
import com.buddyquest.dto.MatchResponse;
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
import com.buddyquest.model.Interest;
import com.buddyquest.repository.InterestRepository;
import com.buddyquest.dto.LeaderboardEntryResponse;
import com.buddyquest.model.PointLedger;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PointLedgerRepository pointLedgerRepository;
    private final InterestRepository interestRepository;

    public UserService(UserRepository userRepository,
                       ProfileRepository profileRepository,
                       PointLedgerRepository pointLedgerRepository,
                        InterestRepository interestRepository){
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.pointLedgerRepository = pointLedgerRepository;
        this.interestRepository = interestRepository;
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
                ledger.getPoints(),
                List.of()
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

        // ⚡️ Вытаскиваем интересы из @ManyToMany
        List<String> interests = user.getInterests().stream()
                .map(Interest::getName)
                .toList();

        return new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                bio,
                points,
                interests
        );
    }
    public void updateUserInterests(UUID userId, List<String> interestNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Set<Interest> newInterests = new HashSet<>();

        for (String rawName : interestNames) {
            String name = rawName.trim().toLowerCase();
            if (name.isEmpty()) {
                continue;
            }

            Interest interest = interestRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> interestRepository.save(new Interest(name)));

            newInterests.add(interest);
        }

        user.setInterests(newInterests);
        userRepository.save(user);
    }
    public UserSummaryResponse loginByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        return getUser(user.getId());
    }

    // ... поля, конструктор, createUser, loginByEmail, getUser и т.д.

    /**
     * Находит пользователей с >=1 общим интересом.
     * Алгоритм:
     *  1. Берём интересы текущего пользователя.
     *  2. Идём по всем пользователям, считаем пересечение интересов.
     *  3. Оставляем только тех, у кого есть пересечение.
     *  4. Сортируем по количеству общих интересов (score) по убыванию.
     */
    public List<MatchResponse> findMatches(UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // множество интересов текущего пользователя (в нижнем регистре)
        Set<String> myInterests = currentUser.getInterests().stream()
                .map(Interest::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (myInterests.isEmpty()) {
            return List.of();
        }

        List<User> allUsers = userRepository.findAll();
        List<MatchResponse> matches = new ArrayList<>();

        for (User candidate : allUsers) {
            if (candidate.getId().equals(userId)) {
                continue; // пропускаем самого себя
            }

            List<String> common = candidate.getInterests().stream()
                    .map(Interest::getName)
                    .map(String::toLowerCase)
                    .filter(myInterests::contains)
                    .distinct()
                    .toList();

            if (!common.isEmpty()) {
                matches.add(new MatchResponse(
                        candidate.getId(),
                        candidate.getDisplayName(),
                        common,
                        common.size()
                ));
            }
        }

        matches.sort(Comparator.comparingInt(MatchResponse::score).reversed());

        return matches;
    }
    public List<LeaderboardEntryResponse> getLeaderboard() {
        List<PointLedger> ledgers = pointLedgerRepository.findTop10ByOrderByPointsDesc();
        List<LeaderboardEntryResponse> result = new ArrayList<>();

        for (PointLedger ledger : ledgers) {
            User user = userRepository.findById(ledger.getUserId())
                    .orElse(null);
            if (user == null) {
                continue;
            }
            result.add(new LeaderboardEntryResponse(
                    user.getId(),
                    user.getDisplayName(),
                    ledger.getPoints()
            ));
        }

        return result;
    }
}
