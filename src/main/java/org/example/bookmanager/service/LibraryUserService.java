package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.LibraryUser;
import org.example.bookmanager.dto.user.LibraryUserDetailDto;
import org.example.bookmanager.dto.user.RequestAddLibraryUser;
import org.example.bookmanager.dto.user.ResponseAddLibraryUser;
import org.example.bookmanager.repository.LibraryUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LibraryUserService {
    private final LibraryUserRepository libraryUserRepository;

    public ResponseAddLibraryUser addLibraryUser(RequestAddLibraryUser request) {
        boolean exists = libraryUserRepository
                .findByNameAndPhone(request.getName(), request.getPhone())
                .isPresent();

        if (exists) {
            return new ResponseAddLibraryUser(false, "이미 등록된 사용자입니다.", null);
        }

        LibraryUser libraryUser = new LibraryUser();
        libraryUser.setName(request.getName());
        libraryUser.setPhone(request.getPhone());
        libraryUser.setMemo(request.getMemo());

        LibraryUser savedUser = libraryUserRepository.save(libraryUser);

        // 저장된 사용자 ID 포함하여 반환
        return new ResponseAddLibraryUser(true, "성공적으로 등록되었습니다.", savedUser.getUserId());
    }


    public LibraryUserDetailDto getUserByID(Long id) {
        LibraryUser user = libraryUserRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("사용자 찾을 수 없음"));

        return new LibraryUserDetailDto(
                user.getUserId(),
                user.getName(),
                user.getPhone(),
                user.getMemo());
    }

    public List<LibraryUserDetailDto> getAllUsers() {
        return libraryUserRepository.findAll()
                .stream()
                .map(user -> new LibraryUserDetailDto(
                        user.getUserId(),
                        user.getName(),
                        user.getPhone(),
                        user.getMemo() // 또는 note
                ))
                .collect(Collectors.toList());
    }

}
