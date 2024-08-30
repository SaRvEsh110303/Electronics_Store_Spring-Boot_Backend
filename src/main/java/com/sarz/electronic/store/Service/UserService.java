package com.sarz.electronic.store.Service;

import com.sarz.electronic.store.dtos.UserDTO;
import com.sarz.electronic.store.dtos.pageableResponse;
import com.sarz.electronic.store.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    //CREATE

    UserDTO CreateUser(UserDTO user);
    //UPDATE
    UserDTO updateUser(UserDTO user, String userId);
    //DELETE
    void deleteUser(String userId) throws IOException;

    //GET ALL ID
    pageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    //GET ONE ID
    UserDTO getId(String id);
    //Get One Email
    UserDTO getEmail(String email);
    
    //OTHERS SPECIFIC DETAILS
    List<UserDTO> searchUser(String keyword);

    Optional<User>  findUserByEmailOptional(String email);
}
