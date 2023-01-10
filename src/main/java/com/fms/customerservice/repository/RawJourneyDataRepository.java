package com.fms.customerservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.RawJourneyDataDTO;

@Repository
public interface RawJourneyDataRepository extends MongoRepository<RawJourneyDataDTO, ObjectId> {
	@Query("{ $and: [{'transport_id':?0},{'gps_create_date': {$gte: ?1, $lte:?2}}]}")
	List<RawJourneyDataDTO> findRawJourneyData(Integer transportId, LocalDateTime fromDate, LocalDateTime toDate);

	@Query("{ 'transport_id':?0 }")
	List<RawJourneyDataDTO> test(Integer transportId);
}
