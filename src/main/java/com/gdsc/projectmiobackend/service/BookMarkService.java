package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.BookMarkDto;
import java.util.List;

public interface BookMarkService {

    void saveBookMark(Long postId , String email);

    List<BookMarkDto> getUserBookMarkList(String email);

}
