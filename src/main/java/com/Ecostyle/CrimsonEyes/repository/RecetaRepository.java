package com.Ecostyle.CrimsonEyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecostyle.CrimsonEyes.model.Receta;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta,String>{
    List<Receta> findByUserId(String userId);
    Receta findByTitleAndUserId(String title, String userId);


}
