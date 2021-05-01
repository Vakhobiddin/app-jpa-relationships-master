package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    //READ
//    @RequestMapping(method = RequestMethod.GET)
    @GetMapping("/read")
    public List<Subject> getSubjects() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList;
    }

    //update
    @PutMapping("/editsubject/{id}")
    public String editSubject(@PathVariable Integer id,Subject subject){
        Optional<Subject> byId = subjectRepository.findById(id);
        if (byId.isPresent()){
            Subject subjectId = byId.get();
            boolean checkName = subjectRepository.existsByName(subject.getName());
            if (checkName){
                return "This subject already exist";
            }
            subjectId.setName(subject.getName());
            subjectRepository.save(subject);
            return "Subject edited";
        }
        return "Something went wrong please try again later";
    }


    //Delete
    @DeleteMapping("/deletesubject/{id}")
    public String deleteSubject(@PathVariable Integer id){
        Optional<Subject> byId = subjectRepository.findById(id);
        if (byId.isPresent()){
            subjectRepository.deleteById(id);
            return "Deleted successfully";
        }

        return "Id not fount";
    }



}
