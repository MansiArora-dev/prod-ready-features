package com.springboot.prodreadyfeatures.repositories;

import com.springboot.prodreadyfeatures.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
