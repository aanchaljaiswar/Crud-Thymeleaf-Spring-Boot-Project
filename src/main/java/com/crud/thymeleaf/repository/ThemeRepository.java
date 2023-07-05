package com.crud.thymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.crud.thymeleaf.entity.Theme;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
  List<Theme> findByTitleContainingIgnoreCase(String keyword);

  @Query("UPDATE Theme t SET t.published = :published WHERE t.id = :id")
  @Modifying
  public void updatePublishedStatus(Integer id, boolean published);
}
