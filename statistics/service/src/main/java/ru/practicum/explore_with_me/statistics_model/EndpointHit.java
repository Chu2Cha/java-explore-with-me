package ru.practicum.explore_with_me.statistics_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpointhits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;
}
