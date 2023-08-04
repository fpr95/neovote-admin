package com.digiteo.neovoteIV.model.jpa.repository;

import com.digiteo.neovoteIV.model.jpa.data.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    //@Query(value= "SELECT MAX(v.)")
    //public int[] findMaxVoteValue();
}
