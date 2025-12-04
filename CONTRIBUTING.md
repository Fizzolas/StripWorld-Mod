# Contributing to StripWorld Mod

Thank you for your interest in contributing to StripWorld Mod! This document provides guidelines for contributing to the project.

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in [Issues](https://github.com/Fizzolas/StripWorld-Mod/issues)
2. If not, create a new issue with:
   - Clear, descriptive title
   - Steps to reproduce the bug
   - Expected vs actual behavior
   - Minecraft version, Forge version, and mod version
   - Relevant logs (from `logs/latest.log`)
   - Screenshots if applicable

### Suggesting Features

1. Check existing issues and pull requests
2. Create a new issue with:
   - Clear description of the feature
   - Use cases and benefits
   - Potential implementation approach (if applicable)

### Submitting Pull Requests

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Make your changes following the code style below
4. Test your changes thoroughly
5. Commit with clear, descriptive messages
6. Push to your fork
7. Create a pull request with:
   - Description of changes
   - Related issue numbers (if applicable)
   - Screenshots/videos for visual changes

## Development Setup

### Prerequisites
- JDK 17
- Git
- IntelliJ IDEA or Eclipse (recommended)

### Setup Steps

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/StripWorld-Mod.git
cd StripWorld-Mod

# Generate IDE files
./gradlew genIntellijRuns  # For IntelliJ
./gradlew genEclipseRuns   # For Eclipse

# Build the project
./gradlew build
```

### Testing

```bash
# Run client for testing
./gradlew runClient

# Run server for testing
./gradlew runServer

# Run data generation
./gradlew runData
```

## Code Style Guidelines

### Java Code
- Use 4 spaces for indentation (no tabs)
- Follow standard Java naming conventions
- Use descriptive variable and method names
- Add comments for complex logic
- Keep methods focused and concise

### Documentation
- Update README.md if adding new features
- Add JavaDoc comments for public methods
- Include inline comments for complex algorithms

### Commits
- Use present tense ("Add feature" not "Added feature")
- Be descriptive but concise
- Reference issue numbers when applicable

Example:
```
Add configurable strip width option

- Add config option for strip width (1-16 chunks)
- Update chunk generator to use config value
- Add validation and error handling

Fixes #123
```

## Project Structure

```
src/main/java/com/fizzolas/stripworld/
├── StripWorldMod.java          # Main mod class
├── config/
│   └── StripWorldConfig.java   # Configuration handling
├── mixin/
│   └── StructurePlacementMixin.java  # Mixins for vanilla code
└── worldgen/
    ├── StripChunkGeneratorWithVoid.java  # Main chunk generator
    ├── StripBiomeSource.java    # Biome distribution
    ├── VoidPatchFeature.java    # Void patch generation
    └── [other world gen files]

src/main/resources/
├── META-INF/
│   └── mods.toml                # Mod metadata
├── mixins.stripworld.json       # Mixin configuration
└── stripworld-common.toml       # Default config
```

## Code Review Process

1. All pull requests require review before merging
2. Address review feedback promptly
3. Keep PRs focused on a single feature/fix
4. Ensure CI builds pass

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

## Questions?

Feel free to ask questions in:
- GitHub Issues (for project-related questions)
- Pull request comments (for PR-specific questions)

Thank you for contributing! 🎉