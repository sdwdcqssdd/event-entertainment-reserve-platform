package com.team20.backend.dto;

public class UserDTOWithRead {
    UserDTO userDTO;
    boolean read;

    public UserDTOWithRead(UserDTO userDTO, boolean read) {
        this.userDTO = userDTO;
        this.read = read;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
