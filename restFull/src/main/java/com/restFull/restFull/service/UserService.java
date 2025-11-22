package com.restFull.restFull.service;

import com.restFull.restFull.dtos.UpdateRequestDTO;
import com.restFull.restFull.dtos.UpdateResponseDTO;
import com.restFull.restFull.dtos.UserResponseDTO;
import com.restFull.restFull.entity.User;
import com.restFull.restFull.exceptions.UsernameAlreadyExistsException;
import com.restFull.restFull.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Metodo auxiliar para pegar o usuário do contexto de segurança
    private User getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário logado não encontrado no banco."));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getAuthenticatedUser() {
        User user = getCurrentUserEntity();
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    @Transactional
    public UpdateResponseDTO updateCurrentUser(UpdateRequestDTO dto) {
        User user = getCurrentUserEntity(); // Pega o usuário do token, não do ID da URL

        String usernameNovo = dto.username();

        // Se mudou o username, verifica se já existe
        if (!user.getUsername().equals(usernameNovo) && userRepository.existsByUsername(usernameNovo)) {
            throw new UsernameAlreadyExistsException("Username já está em uso.");
        }

        user.setUsername(usernameNovo);
        user.setPassword(passwordEncoder.encode(dto.password()));

        User saved = userRepository.save(user);

        return new UpdateResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername()
        );
    }

    @Transactional
    public void deleteCurrentUser() {
        User user = getCurrentUserEntity(); // Garante que deleta a Si Mesmo
        userRepository.delete(user);
    }
}