package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.entity.BookMark;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.BookMarkRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookMarkServiceImpl implements BookMarkService{
    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Override
    public String saveBookMark(Long postId , String email){
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));
        if(bookMarkRepository.findByPostAndUserEntity(post,userEntity) == null){

            if(post.getBookMarkCount() == null){
                post.setBookMarkCount(0L);
            }

            post.setBookMarkCount(post.getBookMarkCount() + 1);

            BookMark bookMark = new BookMark(post,userEntity,true);
            postRepository.save(post);
            bookMarkRepository.save(bookMark);

            return "saveBookMark";
        }
        else {
            BookMark bookMark = bookMarkRepository.findByPostAndUserEntity(post,userEntity);
            post.setBookMarkCount(post.getBookMarkCount() - 1);
            postRepository.save(post);
            bookMarkRepository.delete(bookMark);
            return "deleteBookMark";
        }
    }

    @Override
    public List<BookMark> getUserBookMarkList(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));
        return bookMarkRepository.findByUserEntity(userEntity);
    }
}
