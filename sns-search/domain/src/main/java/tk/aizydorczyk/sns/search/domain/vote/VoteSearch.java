package tk.aizydorczyk.sns.search.domain.vote;

import tk.aizydorczyk.sns.common.domain.vote.VoteType;
import tk.aizydorczyk.sns.search.domain.comment.CommentSearch;
import tk.aizydorczyk.sns.search.domain.post.PostSearch;
import tk.aizydorczyk.sns.search.infrastructure.jpa.BaseSearchEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vote")
public class VoteSearch extends BaseSearchEntity {
    @Enumerated(value = EnumType.STRING)
    private VoteType type;

    @ManyToOne
    private CommentSearch comment;

    @ManyToOne
    private PostSearch post;
}
