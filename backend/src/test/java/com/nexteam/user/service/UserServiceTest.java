package com.nexteam.user.service;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.User.User;
import com.nexteam.features.User.UserRepository;
import com.nexteam.features.User.UserService;
import com.nexteam.features.User.dtos.UserRequestDTO;
import com.nexteam.features.User.dtos.UserResponseDTO;
import com.nexteam.features.User.dtos.mapper.UserMapper;
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
    private UserMapper userMapper;

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

        UserRequestDTO request = UserRequestDTO.builder()
                .firstname("Pierre")
                .lastname("Feuille")
                .email("pierre.feuille@nexteam.com")
                //.password("Password123!")
                .build();

        User user = User.builder()
                .firstname("Pierre")
                .lastname("Feuille")
                .email("pierre.feuille@nexteam.com")
                .password("Password123!")
                .active(true)
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("Pierre")
                .lastname("Feuille")
                .email("pierre.feuille@nexteam.com")
                .active(true)
                .build();


        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(userMapper.requestDTOToUser(request))
                .thenReturn(user);

        when(repository.save(any(User.class)))
                .thenReturn(user);

        when(userMapper.userToResponseDTO(user))
                .thenReturn(response);


        UserResponseDTO result = service.createUser(request);


        assertAll(
                () -> assertEquals("Pierre", result.getFirstname()),
                () -> assertEquals("Feuille", result.getLastname()),
                () -> assertEquals("pierre.feuille@nexteam.com", result.getEmail())
        );


        verify(repository).findByEmail(request.getEmail());
        verify(repository).save(any(User.class));
        verify(userMapper).requestDTOToUser(request);
        verify(userMapper).userToResponseDTO(user);
    }

    @DisplayName("UT-USR-01 - Récupération d'une liste d'utilisateurs")
    @Test
    void getUsers() {

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user1)));


        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("Jean")
                .lastname("Dupont")
                .email("jean.dupont@example.com")
                .build();


        when(userMapper.userToResponseDTO(user1))
                .thenReturn(response);


        Page<UserResponseDTO> users = service.getUsers(Pageable.ofSize(1));


        assertAll(
                () -> assertEquals(1, users.getTotalElements()),
                () -> assertEquals("Jean", users.getContent().getFirst().getFirstname()),
                () -> assertNotNull(users.getContent())
        );


        verify(repository).findAll(any(Pageable.class));
    }

    @DisplayName("UT-USR-02 - Récupération utilisateur existant par id")
    @Test
    void getUser_found() {

        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("Jean")
                .lastname("Dupont")
                .email("jean.dupont@example.com")
                .build();


        when(repository.findByPublicId(user1.getPublicId()))
                .thenReturn(Optional.of(user1));

        when(userMapper.userToResponseDTO(user1))
                .thenReturn(response);


        UserResponseDTO result = service.getUser(user1.getPublicId());


        assertEquals("Jean", result.getFirstname());


        verify(repository).findByPublicId(user1.getPublicId());
        verify(userMapper).userToResponseDTO(user1);
    }

    @DisplayName("UT-USR-03 - Récupération utilisateur par email")
    @Test
    void getUserByEmail_found() {

        String email = "john.doe@example.com";

        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .build();

        when(repository.findByEmail(email))
                .thenReturn(Optional.of(user2));

        when(userMapper.userToResponseDTO(user2))
                .thenReturn(response);

        UserResponseDTO result = service.getUserByEmail(email);

        assertEquals(email, result.getEmail());

        verify(repository).findByEmail(email);
        verify(userMapper).userToResponseDTO(user2);
    }

    @DisplayName("UT-USR-04 - Mise à jour utilisateur valide")
    @Test
    void updateUser() {

        UUID id = user1.getPublicId();


        UserRequestDTO request = UserRequestDTO.builder()
                .firstname("Paul")
                .lastname("Dupont")
                .email(user1.getEmail())
                .build();


        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("Paul")
                .lastname("Dupont")
                .email(user1.getEmail())
                .build();


        when(repository.findByPublicId(id))
                .thenReturn(Optional.of(user1));


        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());


        when(repository.save(any(User.class)))
                .thenReturn(user1);


        when(userMapper.userToResponseDTO(user1))
                .thenReturn(response);


        UserResponseDTO result = service.updateUser(id, request);


        assertEquals("Paul", result.getFirstname());


        verify(repository).findByPublicId(id);
        verify(repository).findByEmail(request.getEmail());
        verify(repository).save(user1);
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
        when(repository.findByPublicId(user1.getPublicId()))
                .thenReturn(Optional.of(user1));

        UserResponseDTO response = UserResponseDTO.builder()
                .firstname("Jean")
                .lastname("Dupont")
                .email("jean.dupont@example.com")
                .build();

        when(userMapper.userToResponseDTO(user1))
                .thenReturn(response);


        assertEquals("Jean", service.getUser(user1.getPublicId()).getFirstname());
    }

    @DisplayName("UT-USR-07 - Email déjà utilisé lors de la modification")
    @Test
    void updateUser_emailExisting() {

        UserRequestDTO request = UserRequestDTO.builder()
                .firstname(user2.getFirstname())
                .lastname(user2.getLastname())
                .email("jean.dupont@example.com")
                .build();


        when(repository.findByPublicId(user2.getPublicId()))
                .thenReturn(Optional.of(user2));


        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user1));


        assertThrows(
                AlreadyExistException.class,
                () -> service.updateUser(
                        user2.getPublicId(),
                        request
                )
        );


        verify(repository).findByPublicId(user2.getPublicId());
        verify(repository).findByEmail(request.getEmail());
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

        UUID unknownId = UUID.fromString(
                "303191e1-7d4d-4c7d-b9bb-380c2e5b6548"
        );


        UserRequestDTO request = UserRequestDTO.builder()
                .firstname("Paul")
                .lastname("Test")
                .email("paul@test.com")
                .build();


        when(repository.findByPublicId(unknownId))
                .thenReturn(Optional.empty());


        assertThrows(
                NotFoundException.class,
                () -> service.updateUser(
                        unknownId,
                        request
                )
        );


        verify(repository).findByPublicId(unknownId);
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

