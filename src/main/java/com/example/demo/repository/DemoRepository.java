package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;
import jakarta.transaction.Transactional;

public interface DemoRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value="update user u set u.salary=:salary where id=:id")
	public void updateSalary(@Param("salary") double salary, @Param("id")int id); 
	
	
	Optional<User> findByEmail(String email);
	
	

}
