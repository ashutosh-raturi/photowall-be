package com.xoriant.photowall.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.photowall.dao.PhotoRepository;
import com.xoriant.photowall.entities.Photo;

@RestController
@CrossOrigin
public class PhotoController {
	
	@Autowired
	private PhotoRepository repo;
	
	@PostMapping(path = "photo")
	public ResponseEntity<Long> uploadPhoto(@RequestParam("image") MultipartFile file,
			@RequestParam("title") String title,@RequestParam("description") String desc) throws IOException
	{
		if(file.isEmpty())
		{
			return ResponseEntity.badRequest().build();
		}
		try
		{
			Photo photo=new Photo();
			photo.setImage(file.getBytes());
			photo.setDescription(desc);
			photo.setTitle(title);
			long id=repo.save(photo).getId();
			return ResponseEntity.ok(id);
		}
		catch(Exception e)
		{
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@DeleteMapping(path = "photo/{id}")
	public ResponseEntity<String> deletePhoto(@PathVariable("id") long id) throws Exception
	{
		try
		{
			repo.deleteById(id);
			return ResponseEntity.ok("Entity deleted successfully");
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity does not exists");
		}
	}
	
	@GetMapping(path = "image/{id}") 
	public ResponseEntity<byte []> getImage(@PathVariable("id") long id) throws EntityNotFoundException
	{
		try
		{
			byte [] image=repo.getById(id).getImage();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
		}
		catch(EntityNotFoundException e)
		{
			return ResponseEntity.notFound().build();
		}
		catch(Exception e)
		{
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping(path="photo/{id}", produces = "application/json")
	public ResponseEntity<HashMap<String,String>> getPhoto(@PathVariable("id") long id) throws EntityNotFoundException
	{
		HashMap<String,String> response=new HashMap<>();
		try
		{
			Photo photo=repo.getById(id);
			response.put("id",""+photo.getId());
			response.put("title",photo.getTitle());
			response.put("description",photo.getDescription());
			response.put("image","http://localhost:8080/image/"+photo.getId());
			return ResponseEntity.ok().body(response);
		}
		catch(EntityNotFoundException e)
		{
			return ResponseEntity.notFound().build();
		}
		catch(Exception e)
		{
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PutMapping(path = "photo/{id}")
	public ResponseEntity<HashMap<String, String>> updatePhoto(@PathVariable("id") long id,
			@RequestParam("title") String title,@RequestParam("description") String desc)
	{
		try
		{
			Photo photo=repo.getById(id);
			photo.setDescription(desc);
			photo.setTitle(title);
			repo.save(photo);
			photo=repo.getById(id);
			HashMap<String,String> response=new HashMap<>();
			response.put("title", photo.getTitle());
			response.put("description",photo.getDescription());
			response.put("id", ""+photo.getId());
			response.put("image", "http://localhost:8080/image/"+photo.getId());
			return ResponseEntity.ok(response);
		}
		catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		catch(Exception e)
		{
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping(path = "photos", produces = "application/json")
	public ResponseEntity<List<HashMap<String, String>>> getPhotos()
	{
		try
		{
			List<Photo> photos=new ArrayList<>();
			List<HashMap<String, String>> response=new ArrayList<>();
			photos=repo.findAll();
			if(photos.isEmpty())
			{
				return ResponseEntity.ok(response);
			}
			photos.forEach(photo->{
				HashMap<String, String> photoObj=new HashMap<>();
				photoObj.put("id", ""+photo.getId());
				photoObj.put("title", photo.getTitle());
				photoObj.put("description", photo.getDescription());
				photoObj.put("image", "http://localhost:8080/image/"+photo.getId());
				response.add(photoObj);
			});
			return ResponseEntity.ok().body(response);
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
