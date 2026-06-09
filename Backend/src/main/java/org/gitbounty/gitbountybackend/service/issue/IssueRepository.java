package org.gitbounty.gitbountybackend.service.issue;

import java.util.List;

import org.gitbounty.gitbountybackend.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByRepositoryName(String repositoryName);
}