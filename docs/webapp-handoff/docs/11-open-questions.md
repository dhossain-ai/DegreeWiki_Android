# Open Questions

These are the main items still needing decisions before Android implementation starts.

- What exact auth method should Android use: Supabase session handling, bearer tokens, or a custom mobile auth layer?
- What is the final mobile API contract for `/api/mobile/*` endpoints?
- Do the `/api/mobile` endpoints already exist, or should they be added later?
- What should the offline cache TTL rules be for programs, universities, scholarships, guides, and countries?
- Should guides and articles return markdown, HTML, or plain text to Android?
- Which push notification provider should Android use later?
- What deep link routes should the Android app support?
- What Play Store package name should be reserved?
- Where should the app icon and public brand assets come from?
- Should saved-result chat, site chat, and Fit Finder each have separate mobile navigation patterns or share a single messaging shell?
- How should offline write actions be reconciled when local queue state conflicts with server state?