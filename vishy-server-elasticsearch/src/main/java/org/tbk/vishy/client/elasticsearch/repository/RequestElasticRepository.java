package org.tbk.vishy.client.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by void on 10.10.15.
 */
public interface RequestElasticRepository extends ElasticsearchCrudRepository<RequestDocument, String> {
}
