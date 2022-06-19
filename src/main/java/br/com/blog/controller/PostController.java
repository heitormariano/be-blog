package br.com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.blog.dto.PostDto;
import br.com.blog.exception.PostNotFoundException;
import br.com.blog.model.Post;
import br.com.blog.service.postService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	postService postService;

	@PostMapping
	public ResponseEntity<Post> createPost(@RequestBody PostDto postDto) {
		try {
			Post _post = postService.createPost(postDto);
			return new ResponseEntity<>(_post, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/all")
	public ResponseEntity<List<PostDto>> showAllPosts() {
		try {
			List<PostDto> postDtoList = postService.showAllPosts();

			if (postDtoList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(postDtoList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getSinglePost(@PathVariable long id) {
		try {
			PostDto postDto = postService.getSinglePost(id);
			if (postDto != null) {
				return new ResponseEntity<>(postDto, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (PostNotFoundException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
