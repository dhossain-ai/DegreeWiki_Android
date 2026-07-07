# Data Model Summary

This is a mobile-focused summary of the main DegreeWiki data entities.

## Countries

Countries are destination pages and comparison anchors. They carry published content, SEO fields, verification data, and destination-specific summaries.

## Universities

Universities belong to countries and often to cities. They provide profile details, official URLs, admissions information, media, and source metadata.

## Programs

Programs are the core searchable entity. They connect to universities, countries, degree levels, subjects, tuition data, deadlines, and official source URLs.

## Scholarships

Scholarships are first-class entities, not just text blocks. They can relate to countries, universities, programs, subjects, and degree levels.

## Articles and guides

Guides and articles provide educational context, SEO content, and explainers that support program discovery and student planning.

## Student profiles

Student profiles belong to authenticated users and store study goals, budget, subject interests, target countries, and other preference data used by Fit Finder and personalization.

## Saved items

Saved items are user-owned references to content the user wants to keep. For Android, this should be one of the first offline-friendly data sets.

## AI Finder results

AI Finder results store the scored shortlist for a student profile, along with explanation text, match metadata, and the saved result identity.

## AI conversations

AI conversations and messages store the user’s chat history. The webapp currently supports saved-result chat and global site chat as separate behaviors.

## Contributor profiles

Contributor profiles exist for reviewed public attribution and later contributor workflows. They are relevant for Android only if contributor features are added later.

## Cross-entity patterns

Most public entities share common metadata patterns such as:

- slug
- summary or intro content
- content status
- verification status
- source confidence
- last verified dates
- canonical and SEO fields
- media references
- published-only visibility rules