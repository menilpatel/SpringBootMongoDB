package com.springmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.springmongo.entity.Blogs;
import com.springmongo.repository.BlogRepository;
import com.springmongo.response.BlogWithUserDetails;

@Service
public class BlogsServices {
	@Autowired
	BlogRepository blogRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Blogs _saveBlogDetails(Blogs blog) {
		return blogRepository.save(blog);
	}

	public List<BlogWithUserDetails> _getAllBlogs() {
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("isPublished").is(true).and("isDeleted").is(false)),
				Aggregation.project().andExpression("$_id").as("id").andExpression("title").as("title")
						.andExpression("description").as("description").andExpression("image").as("image")
						.andExpression("isPublished").as("isPublished").andExpression("isDeleted").as("isDeleted")
						.andExpression("createtimestamp").as("createtimestamp").andExpression("updatetimestamp")
						.as("updatetimestamp"));

		//System.out.println(aggregation);
		AggregationResults<BlogWithUserDetails> results = mongoTemplate.aggregate(aggregation, "blogs",
				BlogWithUserDetails.class);
		return results.getMappedResults();

//		LookupOperation lookupOperation = LookupOperation.newLookup()
//                .from("users")
//                .localField("publishedBy")
//                .foreignField("_id")
//                .as("user");
//
//        UnwindOperation unwindOperation = Aggregation.unwind("user");
//
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("isPublished").is(true).and("isDeleted").is(false)),
//                lookupOperation,
//                unwindOperation,
//                Aggregation.project()
//                        .andExclude("_id")
//                        .andInclude("title", "description", "image")
//                        .and("user.id").as("publishedBy")
//                        .and("user.firstName").as("firstName")
//                        .and("user.middleName").as("middleName")
//                        .and("user.lastName").as("lastName")
//                        .and("user.email").as("email")
//        );
//
//        return mongoTemplate.aggregate(aggregation, "blogs", BlogWithUserDetails.class).getMappedResults();

		// return blogRepository.findByIsDeleted(false);
	}

	public List<Blogs> _getBlogsByAuthor(String userid) {
		return blogRepository.findByPublishedByAndIsDeleted(userid, false);
	}

	public Optional<Blogs> _getBlogById(String id) {
		return blogRepository.findByIdAndIsDeleted(id, false);
	}

	public Blogs _deleteBlogById(String id) {
		Optional<Blogs> objBlog = blogRepository.findByIdAndIsDeleted(id, false);
		if (objBlog.isPresent()) {
			objBlog.get().setIsDeleted(true);
			return _saveBlogDetails(objBlog.get());
		}
		return null;
	}
}
