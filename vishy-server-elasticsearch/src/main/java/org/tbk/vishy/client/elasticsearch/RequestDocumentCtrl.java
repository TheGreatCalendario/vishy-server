package org.tbk.vishy.client.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tbk.vishy.client.elasticsearch.repository.RequestDocument;
import org.tbk.vishy.client.elasticsearch.repository.RequestElasticRepository;

/**
 * Created by void on 10.10.15.
 */
@Controller
@RequestMapping(value = "/elastic/request")
public class RequestDocumentCtrl {

    private RequestElasticRepository repository;

    @Autowired
    public RequestDocumentCtrl(RequestElasticRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Iterable<RequestDocument>> all() {
        final Iterable<RequestDocument> all = repository.findAll();
        return ResponseEntity.ok(all);
    }
}
