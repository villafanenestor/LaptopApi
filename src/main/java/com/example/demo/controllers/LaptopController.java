package com.example.demo.controllers;

import com.example.demo.models.Laptop;
import com.example.demo.repositories.LaptopRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Repositorio de Laptops", description = "Almacen de laptops")
public class LaptopController {
    private String apiUrl = "/laptop";
    private LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping("/api/laptops")
    public List<Laptop> findAll(){
        System.out.println("Buscando");
        return laptopRepository.findAll();
    }

    @PostMapping("/api/laptops")
    public Laptop create(@RequestBody Laptop laptop){
         return  laptopRepository.save(laptop);
    }


    @GetMapping("/api/laptops/{id}")
    public ResponseEntity<Laptop> findOneById(@PathVariable Long id){
        Optional<Laptop> optionalLaptop = laptopRepository.findById(id);
        return optionalLaptop.isPresent() ? ResponseEntity.ok(optionalLaptop.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/api/laptops")
    @ApiResponse(responseCode = "200", description = "Actualizado correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "No se encontro el libro", content = @Content)
    @ApiResponse(responseCode = "400", description = "El campo id esta vacio o es null", content = @Content)
    public ResponseEntity<Laptop> update(@RequestBody Laptop laptop){
        if(laptop.getId() == null || laptop.getId().equals("")){
            return ResponseEntity.badRequest().build();
        }
        if(!laptopRepository.existsById(laptop.getId())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((laptopRepository.save(laptop)));
    }


    @DeleteMapping("/api/laptops/{id}")
    @ApiResponse(responseCode = "200", description = "Se elimino Laptop exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "No existe Laptop con el id enviado", content = @Content)
    public ResponseEntity<String> delete(@PathVariable Long id){
        if(!laptopRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        laptopRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/api/laptops")
    @Tag(name = "Delete All Laptops", description = "Permite eliminar todos las laptops de la base de datos.")
    @ApiResponse(responseCode = "200", description = "Han sido eliminados todos los registros", content = @Content)
    @ApiResponse(responseCode = "404", description = "No hay Laptops en el respositorio", content = @Content)
    public ResponseEntity<String> deleteAll(){
        Long size = laptopRepository.count();
        System.out.println("Cantidad de laptops: "+size);
        if(size<=0){
            return ResponseEntity.notFound().build();
        }
        laptopRepository.deleteAll();

        return ResponseEntity.ok("Eliminados");
    }

}
