package com.nexteam.user.service;

import com.nexteam.exception.AlreadyExistException;
import com.nexteam.exception.NotFoundException;
import com.nexteam.features.user.User;
import com.nexteam.features.user.UserRepository;
import com.nexteam.features.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {

        user1 = User.builder()
                .firstname("Jean")
                .lastname("Dupont")
                .email("jean.dupont@example.com")
                .password("MotDePasse123!")
                .active(true)
                .build();

        user1.setPublicId(UUID.fromString("143191e1-7d4d-4c7d-b9bb-380c2e5b6548"));

        user2 = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("MotDePasse123!")
                .active(true)
                .build();

        user2.setPublicId(UUID.fromString("e6d1585c-7d03-47c6-9e11-b5a7a6281e60"));

    }

    @DisplayName("UT-USR-00 - Création utilisateur valide")
    @Test
    void createUser() {
        User newUser = User.builder()
                .firstname("Pierre")
                .lastname("Feuille")
                .email("pierre.feuille@example.com")
                .password(user2.getPassword())
                .active(user2.isActive())
                .build();

        when(repository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.createUser(newUser);

        assertAll(
                () -> assertEquals("Pierre", result.getFirstname()),
                () -> assertEquals("Feuille", result.getLastname()),
                () -> assertEquals("pierre.feuille@example.com", result.getEmail())
        );

        verify(repository).findByEmail(newUser.getEmail());
        verify(repository).save(newUser);
    }

    @DisplayName("UT-USR-01 - Récupération d'une liste d'utilisatateurs")
    @Test
    void getUsers() {
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(user1), Pageable.ofSize(1), 2));

        Page<User> users = service.getUsers(Pageable.ofSize(1));
        assertAll(
                () -> assertEquals(2, users.getTotalElements()),
                () -> assertEquals(2, users.getTotalPages()),
                () -> assertEquals(user1, users.getContent().getFirst()),
                () -> assertNotEquals(0, users.getTotalElements())
        );

        verify(repository).findAll(any(Pageable.class));
    }

    @DisplayName("UT-USR-02 - Récupération utilisateur existant par id")
    @Test
    void getUser_found() {
        when(repository.findByPublicId(user1.getPublicId())).thenReturn(Optional.of(user1));

        assertEquals(user1, service.getUser(user1.getPublicId()));

        verify(repository).findByPublicId(user1.getPublicId());
    }

    @DisplayName("UT-USR-03 - Récupération utilisateur par email")
    @Test
    void getUserByEmail_found() {
        String email = "john.doe@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.of(user2));

        assertEquals(user2, service.getUserByEmail(email));

        verify(repository).findByEmail(email);

    }

    @DisplayName("UT-USR-04 - Mise à jour utilisateur valide")
    @Test
    void updateUser() {

        User updatedUser = User.builder()
                .firstname("Paul")
                .lastname(user1.getLastname())
                .email(user1.getEmail())
                .password(user1.getPassword())
                .active(user1.isActive())
                .build();

        updatedUser.setPublicId(user2.getPublicId());

        when(repository.findByPublicId(updatedUser.getPublicId()))
                .thenReturn(Optional.of(user1));

        when(repository.findByEmail(updatedUser.getEmail()))
                .thenReturn(Optional.empty());

        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.updateUser(
                updatedUser.getPublicId(),
                updatedUser
        );

        assertEquals("Paul", result.getFirstname());

        verify(repository).findByPublicId(updatedUser.getPublicId());
        verify(repository).findByEmail(updatedUser.getEmail());
        verify(repository).save(any(User.class));
    }

    @DisplayName("UT-USR-05 - Suppression utilisateur par l'id")
    @Test
    void deleteUser_found() {

        when(repository.findByPublicId(user1.getPublicId())).thenReturn(Optional.of(user1));

        service.deleteUser(user1.getPublicId());

        verify(repository).findByPublicId(user1.getPublicId());
        verify(repository).deleteByPublicId(user1.getPublicId());

    }

    @DisplayName("UT-USR-06 - Email déjà existant à la création")
    @Test
    void createUser_emailExisting() {
        User newUser = User.builder()
                .firstname("Pierre")
                .lastname("Feuille")
                .email("jean.dupont@example.com")
                .password(user2.getPassword())
                .active(user2.isActive())
                .build();

        when(repository.findByEmail(newUser.getEmail()))
                .thenReturn(Optional.of(user2));

        assertThrows(AlreadyExistException.class, () -> service.createUser(newUser));

        verify(repository).findByEmail(newUser.getEmail());
    }

    @DisplayName("UT-USR-07 - Email déjà utilisé lors de la modification")
    @Test
    void updateUser_emailExisting() {

        User updatedUser = User.builder()
                .firstname(user2.getFirstname())
                .lastname(user2.getLastname())
                .email("jean.dupont@example.com")
                .password(user2.getPassword())
                .active(user2.isActive())
                .build();

        updatedUser.setPublicId(user2.getPublicId());

        when(repository.findByPublicId(updatedUser.getPublicId()))
                .thenReturn(Optional.ofNullable(user2));

        when(repository.findByEmail(updatedUser.getEmail()))
                .thenReturn(Optional.of(user1));

        assertThrows(AlreadyExistException.class, ()-> service.updateUser(
                updatedUser.getPublicId(),
                updatedUser)
        );

        verify(repository).findByPublicId(updatedUser.getPublicId());
        verify(repository).findByEmail(updatedUser.getEmail());
    }

    @DisplayName("UT-USR-08 - Utilisateur introuvable par l'email lors de la modification")
    @Test
    void getUserByEmail_notfound() {
        String email = "loulou.doe@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUserByEmail(email));

        verify(repository).findByEmail(email);
    }

    @DisplayName("UT-USR-09 - Utilisateur introuvable par l'id lors de la modification")
    @Test
    void updateUser_idNotfound() {

        User updatedUser = User.builder().build();
        updatedUser.setPublicId(UUID.fromString("303191e1-7d4d-4c7d-b9bb-380c2e5b6548"));

        when(repository.findByPublicId(updatedUser.getPublicId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateUser(updatedUser.getPublicId(), updatedUser));

        verify(repository).findByPublicId(updatedUser.getPublicId());
        verify(repository, never()).save(any());
        verify(repository, never()).findByEmail(any());

    }

    @DisplayName("UT-USR-10 - Utilisateur introuvable par l'id lors d'une requete")
    @Test
    void getUser_notFound() {
        UUID unknownId = UUID.fromString("143191e1-7d4d-4c7d-b9bb-380c2e5b6548");
        when(repository.findByPublicId(unknownId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUser(unknownId));

        verify(repository).findByPublicId(unknownId);
    }


    @DisplayName("UT-USR-11 - Suppression utilisateur inexistant par l'id")
    @Test
    void deleteUser_notFound() {

        when(repository.findByPublicId(user1.getPublicId()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.deleteUser(user1.getPublicId())
        );

        verify(repository).findByPublicId(user1.getPublicId());
        verify(repository, never()).deleteByPublicId(any());
    }
}

