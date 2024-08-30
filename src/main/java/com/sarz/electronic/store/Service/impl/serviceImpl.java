package com.sarz.electronic.store.Service.impl;

import com.sarz.electronic.store.Service.UserService;
import com.sarz.electronic.store.dtos.UserDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import com.sarz.electronic.store.entities.Roles;
import com.sarz.electronic.store.entities.User;
import com.sarz.electronic.store.exceptions.ResourceNotFoundException;
import com.sarz.electronic.store.helper.Helper;
import com.sarz.electronic.store.repositories.RolesRepo;
import com.sarz.electronic.store.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class serviceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolesRepo rolesRepo;
    @Value("${normal.role.id}")
    private String role_normal_id;
    @Value("${user.profile.image.path}")
    private String imagePath;
    private Logger logger = LoggerFactory.getLogger(serviceImpl.class);
    @Override
    public UserDTO CreateUser(UserDTO userDTO) {
        //GENERATE UNIQUE ID
        String userId = UUID.randomUUID().toString();
        userDTO.setUserId(userId);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user= dtoToEntity(userDTO);
        Roles roles = rolesRepo.findById(role_normal_id).get();
         user.getRoles().add(roles);
        User saveUser=userRepo.save(user);
        UserDTO newDto=entityToDTO(saveUser);
        return newDto;
    }

    @Override
    public UserDTO updateUser(UserDTO user, String userId) {
        User update = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Id not Found"));
        update.setName(user.getName());
        update.setAbout(user.getAbout());
        update.setGender(user.getGender());
        update.setPassword(user.getPassword());
        update.setImageName(user.getImageName());
        User save = userRepo.save(update);
        UserDTO userDTO = entityToDTO(save);
        return userDTO;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID not found"));
        String fullPath = imagePath + user.getImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepo.delete(user);
    }
    @Override
    public pageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepo.findAll(pageable);
        pageableResponse<UserDTO> response = Helper.getPageableResponse(page, UserDTO.class);

        return response;
    }

    @Override
    public UserDTO getId(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User ID not Found!!"));
        return entityToDTO(user);
    }

    @Override
    public UserDTO getEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email not Found!!"));
        return entityToDTO(user);
    }

    @Override
    public List<UserDTO> searchUser(String keyword) {
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDTO> userDTOList = users.stream().map(user -> entityToDTO(user)).collect(Collectors.toList());
        return userDTOList;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {

        return userRepo.findByEmail(email);
    }


    private UserDTO entityToDTO(User saveUser) {
//        UserDTO build = UserDTO.builder()
//                .userId(saveUser.getUserId())
//                .name(saveUser.getName())
//                .email(saveUser.getEmail())
//                .password(saveUser.getPassword())
//                .about(saveUser.getAbout())
//                .gender(saveUser.getGender())
//                .imageName(saveUser.getImageName()).build();
//
            return mapper.map(saveUser,UserDTO.class);
    }
    private User dtoToEntity(UserDTO userDTO) {
//        User build = User.builder()
//                .userId(userDTO.getUserId())
//                .name(userDTO.getName())
//                .email(userDTO.getEmail())
//                .password(userDTO.getPassword())
//                .about(userDTO.getAbout())
//                .gender(userDTO.getGender())
//                .imageName(userDTO.getImageName()).build();
        return mapper.map(userDTO,User.class);
    }

}