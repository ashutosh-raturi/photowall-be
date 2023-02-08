package com.xoriant.photowall.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.xoriant.photowall.entities.Photo;

@Component
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
