package br.com.blog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import br.com.blog.dto.PostDto;
import br.com.blog.exception.PostNotFoundException;
import br.com.blog.model.Post;
import br.com.blog.repository.PostRepository;

@Service
public class postService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	AuthService authService;

	public Post createPost(PostDto postDto) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		User currentUser = authService.getCurrentUser()
				.orElseThrow(() -> new IllegalArgumentException("No user logged in"));
		post.setUserName(currentUser.getUsername());
		post.setCreatedOn(Instant.now());

		return postRepository.save(post);
	}

	public List<PostDto> showAllPosts() {

		List<Post> posts = postRepository.findAll();
		return posts.stream().map(this::mapFromPostToDto).collect(Collectors.toList());
	}

	public PostDto getSinglePost(long id) throws PostNotFoundException {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
		return mapFromPostToDto(post);
	}

	private PostDto mapFromPostToDto(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setContent(post.getContent());
		postDto.setUserName(post.getUserName());

		return postDto;
	}

	// private Post mapFromPostDtoToPost(PostDto postDto) {
	// 	Post post = new Post();
	// 	post.setId(postDto.getId());
	// 	post.setTitle(postDto.getTitle());
	// 	post.setContent(postDto.getContent());
	// 	post.setUserName(postDto.getUserName());

	// 	return post;
	// }
}
