package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDTO;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 5);

        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);

    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{id}")
    public Page<Student>getStudentListForFaculty(@PathVariable Integer id,
                                                 @RequestParam int page){
        Pageable pageable = PageRequest.of(page,5);
        return studentRepository.findAllByGroup_Faculty_Id(id,pageable);

    }
    //4. GROUP OWNER
    @GetMapping("/forGroupOwner/{groupId}")
    public Page<Student>getStudentList(@PathVariable Integer groupId,
                                       @RequestParam int page){
        Pageable pageable = PageRequest.of(page,5);
        return studentRepository.findByGroup_Id(groupId,pageable);
    }

    @GetMapping("getStudent/{id}")
    public Student getOne(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElseGet(Student::new);

    }


    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDTO studentDTO){
        Student student = new Student();
        boolean exist  = studentRepository.existsStudentByFirstNameAndLastNameAndGroup_Id(studentDTO.getFirstName(), studentDTO.getLastName(),
                studentDTO.getGroup_id());
        if (exist){
            return "This student already exist";
        }

        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());

        Address address = new Address();
        address.setCity(studentDTO.getCity());
        address.setDistrict(studentDTO.getDistrict());
        address.setStreet(studentDTO.getStreet());
        student.setAddress(address);
        addressRepository.save(address);

        Optional<Group> groupRepositoryById = groupRepository.findById(studentDTO.getGroup_id());
        if (groupRepositoryById.isPresent()){
            Group group = groupRepositoryById.get();
            student.setGroup(group);
        }else {
            return "Group id not fount";
        }

        List<Integer>subjects = studentDTO.getSubject_id();
        List<Subject> subjectList = new ArrayList<>();

        for (Integer subject : subjects) {
            Optional<Subject> subjectRepositoryById = subjectRepository.findById(subject);
            if (subjectRepositoryById.isPresent()){
                Subject studentSubjects = subjectRepositoryById.get();
                subjectList.add(studentSubjects);
                student.setSubjects(subjectList);
            }else {
                return "Subject id not fount";
            }
        }

        studentRepository.save(student);
        return "Successfully added";
    }

    @PutMapping("/editStudent/{id}")
    public String editStudent(@PathVariable Integer id,@RequestBody StudentDTO studentDTO){
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()){
            boolean exist  = studentRepository.existsStudentByFirstNameAndLastNameAndGroup_Id(studentDTO.getFirstName(),
                    studentDTO.getLastName(),studentDTO.getGroup_id());
            if (exist){
                return "This student already exist";
            }

            Student student = byId.get();
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());

            Address address = student.getAddress();
            address.setStreet(studentDTO.getStreet());
            address.setDistrict(studentDTO.getDistrict());
            address.setCity(studentDTO.getCity());

            student.setAddress(address);

            Optional<Group> groupRepositoryById = groupRepository.findById(studentDTO.getGroup_id());
            if (groupRepositoryById.isPresent()){
                student.setGroup(groupRepositoryById.get());
            }else {
                return "Group id not fount";
            }

            List<Integer> integers = studentDTO.getSubject_id();
            List<Subject> subjectList = new ArrayList<>();

            for (Integer subject : integers) {
                Optional<Subject> subjectRepositoryById = subjectRepository.findById(subject);
                if (subjectRepositoryById.isPresent()){
                    Subject studentSubjects = subjectRepositoryById.get();
                    subjectList.add(studentSubjects);
                    student.setSubjects(subjectList);
                }else {
                    return "Subject id not fount";
                }
            }
            studentRepository.save(student);
            return "Edited successfully";
        }

        return "Student id not fount";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteOne(@PathVariable Integer id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "Student deleted successfully";
        }
        return "Student is not found";
    }


}
