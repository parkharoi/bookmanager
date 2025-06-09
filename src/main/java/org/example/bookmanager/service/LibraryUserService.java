package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmanager.domain.LibraryUser;
import org.example.bookmanager.dto.RequestAddLibraryUser;
import org.example.bookmanager.dto.ResponseAddLibraryUser;
import org.example.bookmanager.repository.LibraryUserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryUserService {
    private final LibraryUserRepository libraryUserRepository;

    public ResponseAddLibraryUser addLibraryUser(RequestAddLibraryUser request) {
        boolean exists = libraryUserRepository
                .findByNameAndPhone(request.getName(), request.getPhone())
                .isPresent();

        if (exists) {
            return new ResponseAddLibraryUser(false, "이미 등록된 사용자입니다.");
        }

        LibraryUser libraryUser = new LibraryUser();
        libraryUser.setName(request.getName());
        libraryUser.setPhone(request.getPhone());
        libraryUser.setMemo(request.getMemo());

        libraryUserRepository.save(libraryUser);

        return new ResponseAddLibraryUser(true, "성공적으로 등록되었습니다.");
    }

}
