package com.springmongo.services;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
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
		AggregationOperation addFields = context -> new Document("$addFields",
				new Document("publishedBy", new Document("$toObjectId", "$publishedBy")));

		AggregationOperation lookup = context -> new Document("$lookup", new Document("from", "users")
				.append("localField", "publishedBy").append("foreignField", "_id").append("as", "user_info"));

		AggregationOperation unwind = context -> new Document("$unwind", "$user_info");

		AggregationOperation project = context -> new Document("$project",
				new Document("title", 1).append("description", 1).append("image", 1).append("isPublished", 1)
						.append("isDeleted", 1).append("createtimestamp", 1).append("updatetimestamp", 1)
						.append("user", "$user_info"));

		Aggregation aggregation = Aggregation.newAggregation(addFields, lookup, unwind, project);

		AggregationResults<BlogWithUserDetails> results = mongoTemplate.aggregate(aggregation, "blogs",
				BlogWithUserDetails.class);
		return results.getMappedResults();
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
