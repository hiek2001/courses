package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
    // select * from folder where user_id = ? and name in (?, ?, ?);
    // 전체를 찾겠다. User 기준으로 찾을건데 뿐만 아니라 여러개의 폴더 이름도 함께 찾겠다.


    List<Folder> findAllByUser(User user);
    // select * from folder where user_id = ?


}
