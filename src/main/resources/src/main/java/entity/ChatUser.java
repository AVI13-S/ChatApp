package entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "chat_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    protected ChatUser() {
        // required by JPA
    }

    public ChatUser(String username) {
        this.username = username;
        enter();
    }

    public void enter() {
        Instant now = Instant.now();
        this.active = true;
        this.joinedAt = now;
        this.lastSeenAt = now;
    }

    public void leave() {
        this.active = false;
    }

    public void touch() {
        this.lastSeenAt = Instant.now();
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public boolean isActive() { return active; }

    public Instant getJoinedAt() { return joinedAt; }

    public Instant getLastSeenAt() { return lastSeenAt; }
}
