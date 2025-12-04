# StripWorld Mod

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)
![Forge](https://img.shields.io/badge/Forge-47.3.0-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A Minecraft 1.20.1 Forge mod that transforms world generation into infinite 2-chunk-wide strips with buildable void patches beneath bedrock.

## Features

### 🌍 Strip World Generation
- **2-Chunk Wide Strips**: Worlds generate as 32-block-wide strips (configurable)
- **Infinite Length**: No restrictions on Z-axis or height (Y-axis)
- **All Dimensions**: Works in Overworld, Nether, and End
- **Full Biome Support**: All vanilla biomes generate naturally within strips
- **Structure Compatible**: Villages, temples, strongholds, and all vanilla structures spawn correctly

### 🕳️ Void Patch System
- **Irregular Holes**: Non-uniform circular void patches in the bedrock layer
- **Buildable Void**: Players can build in void areas down to the kill zone (-64)
- **Perlin Noise Generation**: Organic, natural-looking void patterns
- **Configurable**: Adjust size, frequency, and appearance of void patches

### 🔧 Mod Compatibility
- **Open-Ended Design**: Compatible with other mods like Create
- **Structure Preservation**: Automatically adjusts structure placement to respect strip boundaries
- **Minimal Mixins**: Uses only essential mixins for maximum compatibility

## Installation

### Prerequisites
- Minecraft 1.20.1
- Forge 47.3.0 or higher
- Java 17

### Steps
1. Download the latest release from the [Releases](https://github.com/Fizzolas/StripWorld-Mod/releases) page
2. Place the `.jar` file in your `mods` folder
3. Launch Minecraft with Forge 1.20.1
4. Create a new world and select "Strip World" as the world type

## Configuration

Configuration file: `config/stripworld-common.toml`

```toml
[worldgen]
    # Width of strips in chunks (default: 2)
    stripWidthChunks = 2
    
    # Enable void patch generation
    generateVoidPatches = true
    
    # Void patch size range (blocks)
    voidPatchMinRadius = 5
    voidPatchMaxRadius = 20
    
    # Void patch frequency (0.0 to 1.0)
    voidPatchFrequency = 0.15
    
    # Enable for specific dimensions
    enableNetherStrips = true
    enableEndStrips = true

[compatibility]
    # Allow other mods to modify generation
    allowModCompat = true
    
    # Preserve vanilla structure spacing
    preserveStructureSpacing = true
```

## How It Works

### Strip Generation
The mod uses a custom `ChunkGenerator` that:
1. Restricts chunk generation to 2-chunk-wide boundaries on the X-axis
2. Maintains unlimited generation on the Z-axis (length)
3. Preserves full Y-axis height (including new 1.18+ height limits)
4. Fills areas outside strips with air/void

### Biome Distribution
Biomes are constrained to strip boundaries using a custom `BiomeSource` that:
- Maps X-coordinates to equivalent positions within a single strip
- Ensures consistent biome patterns across parallel strips
- Preserves vanilla biome generation logic

### Void Patches
Void patches are generated during chunk filling using:
- **Perlin Simplex Noise** for irregular shapes
- **Deterministic Seeding** based on chunk position
- **Selective Removal** of bedrock blocks (some patches remain for visual interest)
- **Cave Air Blocks** for proper lighting and buildability

### Structure Adaptation
A mixin intercepts structure placement to:
- Detect structures attempting to spawn outside strips
- Shift them to the nearest valid strip position
- Maintain proper spacing and distribution
- Support both vanilla and modded structures

## Building from Source

### Prerequisites
- JDK 17
- Git

### Build Steps
```bash
# Clone the repository
git clone https://github.com/Fizzolas/StripWorld-Mod.git
cd StripWorld-Mod

# Build with Gradle (wrapper included)
./gradlew build

# Output JAR will be in build/libs/
```

### Development Setup
```bash
# Generate IDE files
./gradlew genIntellijRuns  # For IntelliJ IDEA
./gradlew genEclipseRuns   # For Eclipse

# Run client
./gradlew runClient

# Run server
./gradlew runServer
```

## Technical Details

### Core Components

**World Generation**
- `StripChunkGeneratorWithVoid` - Main chunk generator with strip and void logic
- `StripBiomeSource` - Biome source that constrains to strip boundaries
- `StripWorldPreset` - World preset registration for all dimensions

**Void Generation**
- `VoidPatchFeature` - Feature for carving void holes
- `VoidPatchConfig` - Configuration for void patch parameters

**Structure Handling**
- `StructurePlacementMixin` - Adjusts structure placement to strips
- `StripStructureHandler` - Utility class for boundary checks

**Configuration**
- `StripWorldConfig` - Forge config system integration

### Performance Considerations
- Void chunks are lightweight (filled with air)
- Perlin noise calculations are optimized
- Structure adjustments happen during placement, not runtime
- Compatible with chunk pre-generation mods

## FAQ

**Q: Can I change the strip width?**  
A: Yes! Edit `stripWidthChunks` in the config file. Values from 1-16 are supported.

**Q: Will this work with mod X?**  
A: Most mods should work fine. The mod is designed for compatibility, especially with content mods like Create.

**Q: Can I disable void patches?**  
A: Yes! Set `generateVoidPatches = false` in the config.

**Q: Do structures still generate?**  
A: Yes! All vanilla structures (villages, temples, strongholds, etc.) generate within the strips.

**Q: Can I build in the void areas?**  
A: Yes! Void areas are buildable down to the kill zone at y=-64.

**Q: Does this affect existing worlds?**  
A: No, this mod only affects newly generated chunks. It's recommended to create a new world.

## Compatibility

### Tested Compatible Mods
- Create (and most Create addons)
- Biomes O' Plenty
- Terralith (datapack version)
- JEI/REI
- Journey Map / other map mods

### Known Issues
- Some large structures may extend beyond strip boundaries (automatically adjusted)
- Chunk pre-generation mods should work but may need strip boundaries explained

## Contributing

Contributions are welcome! Please feel free to:
- Report bugs via [Issues](https://github.com/Fizzolas/StripWorld-Mod/issues)
- Submit pull requests with improvements
- Suggest features or enhancements

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

**Author**: Fizzolas  
**Minecraft Version**: 1.20.1  
**Forge Version**: 47.3.0+  

## Changelog

### v1.0.0 (Initial Release)
- 2-chunk wide strip generation
- Void patch system with Perlin noise
- Full dimension support (Overworld, Nether, End)
- Structure adaptation system
- Configuration support
- Mod compatibility framework

---

**Enjoy your infinite strip worlds!** 🌍✨