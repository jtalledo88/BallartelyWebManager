package pe.com.foxsoft.ballartelyweb.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Invoice;

@Repository
public interface FacturaRepository extends JpaRepository<Invoice, Integer>{
	
	public List<Invoice> findByGuideHead(GuideHead guideHead);
	
	
}
