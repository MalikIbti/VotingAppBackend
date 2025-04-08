package com.voting.votingApp.services;

import com.voting.votingApp.model.OptionVote;
import com.voting.votingApp.model.Poll;
import com.voting.votingApp.repositories.PollRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PollService {
    private PollRepository pollRepository;
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll createPoll(Poll poll) {
        System.out.println("Saving poll: " + poll);
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public ResponseEntity<Poll> getPollById(Long id) {
        return pollRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public void vote(Long pollId, int optionIndex) {
        //get the poll from DB
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(()-> new RuntimeException("Poll not found"));

        //get all options
        List<OptionVote> options = poll.getOptions();

        //if index for vote is not valid, throw error
        if(optionIndex<0 || optionIndex>=options.size()){
            throw new IllegalArgumentException("invalid option index");
        }

        //get selected option
        OptionVote selectedOption = options.get(optionIndex);

        //increment vote for selected option
        selectedOption.setVoteCount(selectedOption.getVoteCount()+1);

        // save incremented option into the DB
        pollRepository.save(poll);
    }
}
