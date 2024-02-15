package com.example.dataManagerWebApp;

import com.example.ResourceClasses.Adress;
import com.example.ResourceClasses.Person;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdressController {
    @GetMapping("/adress")
    public List<Adress> getAllAdresses(){
        return PersonStorage.getAllAdresses();
    }
    @GetMapping("/adress/{id}")
    public Adress getAdressById(@PathVariable String id){
        if (PersonStorage.getAdressById(id) != null) {
            return PersonStorage.getAdressById(id);
        }
        else {
            throw new AdressNotFoundException();
        }
    }

    @PostMapping("/adress")
    public Adress createAdress(@RequestBody Adress newAdress){
        newAdress.setId("a"+(PersonStorage.getAllAdresses().size()+1));
        PersonStorage.saveAdress(newAdress);
        return newAdress;
    }

    @PutMapping("/adress")
    public Adress updateAdress(@RequestBody Adress updatedAdress){
        Adress adress = PersonStorage.getAdressById(updatedAdress.getId());
        adress.setCity(updatedAdress.getCity());
        adress.setPermanentAdress(updatedAdress.isPermanentAdress());
        adress.setContacts(updatedAdress.getContacts());
        PersonStorage.saveAdress(adress);
        return adress;
    }

    @DeleteMapping("/adress/{id}")
    public Adress deleteAdress(@PathVariable String id) {
        Adress adress = PersonStorage.getAdressById(id);
        if (adress != null) {
            PersonStorage.deleteAdress(PersonStorage.getAdressById(id));
            return adress;
        }
        else {
            throw new AdressNotFoundException();
        }
    }

}
