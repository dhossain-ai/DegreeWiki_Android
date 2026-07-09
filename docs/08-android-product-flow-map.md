# Android Product Flow Map

Last updated: 2026-07-10

## A. Purpose

This file maps the current DegreeWiki Android public flow so future bundles stay aligned with the student-first browse product.

## B. Public Access Without Login

Public users can currently access:

- Home
- Programs
- Program details
- Universities
- University details
- Destinations
- Country details

Public users may later access, once implemented safely:

- Scholarships
- Guides and articles
- Search/filter improvements
- Compare or save-related flows where product rules allow

## C. Login-Required Access

Login is still required for:

- Saved-item account flows
- Personalized profile content
- Fit Finder follow-up flows when implemented
- Chat/history flows when implemented

## D. Bottom Navigation

Current bottom navigation:

- Home
- Programs
- Universities
- Destinations
- Profile

Chat is not a bottom tab.

Fit Finder is not a bottom tab.

Scholarships and Guides are not bottom tabs.

## E. Home Surface

Home is now the target first tab and current app entry surface.

Home currently includes:

- DegreeWiki heading and intro copy
- search-entry card routed into Programs browse
- trust/source note
- quick browse cards for Programs, Universities, and Destinations
- real featured content only when cache-backed data already exists
- deferred messaging for Fit Finder, scholarships, guides, and question/help

## F. Deferred Public Surfaces

- Fit Finder remains a future CTA, not a working Android feature
- Chat remains a future small help surface, not a main dashboard
- Scholarships and Guides remain future public surfaces until safe Android routes exist

## G. Data Truth Rule

Android must show only API or cache-backed data.

Do not invent:

- programs
- universities
- destinations
- scholarships
- tuition
- deadlines
- verification/source claims
- AI or Fit Finder results
