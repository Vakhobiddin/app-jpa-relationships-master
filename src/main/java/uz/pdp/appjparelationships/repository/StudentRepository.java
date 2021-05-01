package uz.pdp.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {


    Page<Student> findAllByGroup_Faculty_UniversityId(Integer group_faculty_university_id, Pageable pageable);

    Page<Student> findAllByGroup_Faculty_Id(Integer faculty_id,Pageable pageable);

    Page<Student> findByGroup_Id(Integer group_id,Pageable pageable);

    boolean existsStudentByFirstNameAndLastNameAndGroup_Id(String FirstName, String LastName,Integer group_id);





}
