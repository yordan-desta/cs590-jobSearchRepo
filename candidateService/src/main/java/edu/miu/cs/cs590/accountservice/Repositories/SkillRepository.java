package edu.miu.cs.cs590.accountservice.Repositories;


import edu.miu.cs.cs590.accountservice.Models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  
public interface SkillRepository extends JpaRepository<Skill, Long> {
}

 