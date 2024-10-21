package tn.esprit.spring.services.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.services.entity.Universite;

@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long>
{


}
