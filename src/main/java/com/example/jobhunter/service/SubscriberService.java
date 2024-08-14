package com.example.jobhunter.service;

import com.example.jobhunter.entity.Skill;
import com.example.jobhunter.entity.Subscriber;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.SkillRepository;
import com.example.jobhunter.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    SkillRepository skillRepository;
    public Subscriber handlerCreateSubcriber(Subscriber subscriber) throws IdInvalidException {
        Boolean checkEmail= subscriberRepository.existsByEmail(subscriber.getEmail());
        if(checkEmail){
            throw new IdInvalidException("email da ton tai");
        }
        if(subscriber.getSkills()!=null){
            List<Long> skillId=subscriber.getSkills().stream().map(item->item.getId()).toList();
            List<Skill> dbSkill=skillRepository.findByIdIn(skillId);
            subscriber.setSkills(dbSkill);
        }
        return subscriberRepository.save(subscriber);
    }
    public Subscriber handleUpdateSubscriber(Subscriber subscriber) throws IdInvalidException{
        Optional<Subscriber> optionalSubscriber= subscriberRepository.findById(subscriber.getId());
        if(optionalSubscriber.isEmpty()){
            throw new IdInvalidException("khong ton tai sub");
        }
        Subscriber currentSub=optionalSubscriber.get();
        if(subscriber.getSkills()!=null){
            List<Long> skillId=subscriber.getSkills().stream().map(item->item.getId()).toList();
            List<Skill> dbSkill=skillRepository.findByIdIn(skillId);
            currentSub.setSkills(dbSkill);
        }
        return subscriberRepository.save(currentSub);
    }
}
