package pe.com.foxsoft.ballartelyweb.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;

@Repository
public interface GuiaCabeceraRepository extends JpaRepository<GuideHead, Integer>{
	
}
