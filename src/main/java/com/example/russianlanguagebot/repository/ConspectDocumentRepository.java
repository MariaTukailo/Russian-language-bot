package com.example.russianlanguagebot.repository;

import com.example.russianlanguagebot.entity.ConspectDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConspectDocumentRepository extends JpaRepository<ConspectDocument, Long> {
}