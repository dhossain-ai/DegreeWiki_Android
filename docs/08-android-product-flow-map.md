# Android Product Flow Map

Last updated: 2026-07-10

## A. Purpose

This file maps DegreeWiki web product behavior into the intended native Android mobile flow so future Android bundles stay aligned with the student-first product.

## B. Public Access Without Login

Public users can browse without an account:

- Home later
- Programs
- Program details
- Universities
- University details
- Countries and destinations
- Country details
- Scholarships later
- Guides and articles later
- Search and filter later
- Basic local compare later, if implemented without account sync

## C. Login-Required Access

Login is required for:

- Save or unsave programs or scholarships
- Saved items
- Student dashboard and profile
- Running or saving Fit Finder results, unless backend later supports anonymous trials
- Fit Finder result chat
- Personalized chat and history
- Account settings

## D. Bottom Navigation Direction

Near-term target bottom navigation:

- Home
- Programs
- Universities
- Destinations
- Profile

Do not implement Home in this bundle. This section documents the target only.

## E. Home Screen Future Role

Home should later include:

- DegreeWiki header
- Search card
- Quick browse cards
- Trust and source note
- Fit Finder CTA, login-gated
- Scholarship and guide entry cards
- Featured real database content only
- Small general help or chat entry, not a main AI dashboard

## F. Detail Screen Target Behavior

Program detail should eventually show only real API or cache-backed fields:

- title
- university
- country and city
- degree level
- subject
- tuition if available
- duration if available
- language if available
- deadline and intake if available
- admission requirements if available
- official or source link if available
- save, login-gated
- compare local or public later if supported

University detail should eventually show:

- university name
- country and city
- overview
- official website or source cues if available
- related programs if available

Country detail should eventually show:

- country name
- overview
- study facts if available
- tuition and living cost if available
- visa and work guidance if available
- related universities and programs if available

## G. Chat Placement

Chat should not be a main bottom navigation tab for now.

Later placement:

- small Home help card
- contextual support inside Fit Finder results
- maybe program or detail help later

Chat must use the backend AI gateway or static-answer route, not local AI logic.

## H. Fit Finder Placement

Fit Finder should be a Home CTA later.

If the user is logged out, route to login with a clear explanation.

Do not implement Fit Finder in this bundle.

## I. Data Truth Rule

Android must show only API or cache-backed data. Do not invent universities, programs, scholarships, tuition, deadlines, source status, or verification claims.
