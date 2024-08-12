package com.jay9971.VTBuilder.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Long>{

    @Query("SELECT MAX(u.id) FROM Users u")
	Long findMaxId();
}
