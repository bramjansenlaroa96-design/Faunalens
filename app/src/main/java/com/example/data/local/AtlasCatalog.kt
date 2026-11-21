package com.example.data.local

import com.example.domain.model.AnimalClass
import com.example.domain.model.ConservationStatus
import com.example.domain.model.SpeciesInfo
import com.example.domain.model.TaxonomyTree

object AtlasCatalog {
    val entries: List<SpeciesInfo> = listOf(
        SpeciesInfo(
            id = "atlas_red_fox",
            commonName = "Red Fox",
            scientificName = "Vulpes vulpes",
            animalClass = AnimalClass.MAMMAL,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Mammalia",
                order = "Carnivora",
                family = "Canidae",
                genus = "Vulpes",
                species = "V. vulpes"
            ),
            conservationStatus = ConservationStatus.LEAST_CONCERN,
            confidencePercent = 96,
            summary = "The most widely distributed wild member of the order Carnivora, renowned for its russet coat, bushy white-tipped tail, and remarkable adaptability across forests, grasslands, and urban edges.",
            sizeAndMass = "Length 45–90 cm (excl. tail) • Mass 3–14 kg",
            lifespan = "2–5 years wild (up to 14 years in care)",
            activityPattern = "Crepuscular & Nocturnal",
            physicalTraits = "Rust-orange dorsal fur with pale cream chest and belly, black ear tips and lower legs ('socks'), narrow muzzle, vertically slit pupils adapted for low-light hunting, and a thick 30–55 cm brush tail tipped with bright white.",
            habitatAndRange = "Native across the entire Northern Hemisphere from the Arctic Circle to North Africa, Central America, and temperate Asia; introduced in Australia. Thrives in mixed woodland, farmland, tundra, and suburban parks.",
            dietAndEcology = "Opportunistic omnivore consuming small rodents, lagomorphs, birds, beetles, earthworms, berries, and fallen fruit. Plays a vital role regulating small mammal populations.",
            behaviorAndReproduction = "Uses Earth's magnetic field ('mousing' leap) to pinpoint prey beneath snow or tall grass. Primarily solitary hunters outside breeding season (Dec–Feb); litters of 4–6 kits are raised in earthen dens by both parents.",
            safetyAndInteraction = "Generally shy and non-aggressive toward humans. Keep at least 10 meters distance, never feed wild foxes, and ensure pets are vaccinated as foxes can carry rabies or sarcoptic mange.",
            funFacts = listOf(
                "Its whiskers on both face and wrists help it navigate dense brush in near-total darkness.",
                "Over 28 distinct vocalizations have been documented, including sharp 'gekkering' calls.",
                "Its bushy tail acts as both a balance rudder during sprints and a warm wrap in winter."
            ),
            suggestedQuestions = listOf(
                "How do I distinguish a Red Fox from a Coyote?",
                "Why do Red Foxes jump high before pouncing?",
                "What should I do if I see one during the day?"
            )
        ),
        SpeciesInfo(
            id = "atlas_common_kingfisher",
            commonName = "Common Kingfisher",
            scientificName = "Alcedo atthis",
            animalClass = AnimalClass.BIRD,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Aves",
                order = "Coraciiformes",
                family = "Alcedinidae",
                genus = "Alcedo",
                species = "A. atthis"
            ),
            conservationStatus = ConservationStatus.LEAST_CONCERN,
            confidencePercent = 95,
            summary = "A compact, jewel-toned river bird with iridescent electric-blue upperparts, vivid rufous-orange underparts, and a dagger-like bill built for high-speed plunge-diving.",
            sizeAndMass = "Length 16–17 cm • Wingspan 25 cm • Mass 34–46 g",
            lifespan = "2–7 years",
            activityPattern = "Diurnal",
            physicalTraits = "Metallic cobalt-blue and emerald-turquoise back and crown (produced by structural feather light scattering), bright chestnut belly and ear patches, white throat and neck flash, and a long black bill (lower mandible orange-red in females).",
            habitatAndRange = "Widespread across Europe, North Africa, and temperate to tropical Asia. Favors slow-moving, clear streams, rivers, lakes, and reed-fringed canals with overhanging perches.",
            dietAndEcology = "Piscivorous specialist feeding mainly on small fish (minnows, sticklebacks, bullheads) under 6 cm, supplemented by aquatic insects and tadpoles. Serves as a sensitive bioindicator of clean freshwater ecosystems.",
            behaviorAndReproduction = "Hunts from low branches 1–2 meters above water, compensating for light refraction before diving vertically. Excavates 60–90 cm horizontal nest tunnels into vertical sandy riverbanks.",
            safetyAndInteraction = "Harmless to humans; easily disturbed near nesting banks. Observe quietly with binoculars without approaching riverbank burrows.",
            funFacts = listOf(
                "Its eyes switch from monocular to binocular vision the instant it pierces the water surface.",
                "A third translucent eyelid (nictitating membrane) protects its eyes underwater.",
                "Its brilliant blue plumage contains no blue pigment—it is purely structural color."
            ),
            suggestedQuestions = listOf(
                "How can you tell male and female Common Kingfishers apart?",
                "How fast do they dive into water?",
                "Why is their plumage electric blue?"
            )
        ),
        SpeciesInfo(
            id = "atlas_monarch_butterfly",
            commonName = "Monarch Butterfly",
            scientificName = "Danaus plexippus",
            animalClass = AnimalClass.ARTHROPOD,
            taxonomy = TaxonomyTree(
                phylum = "Arthropoda",
                className = "Insecta",
                order = "Lepidoptera",
                family = "Nymphalidae",
                genus = "Danaus",
                species = "D. plexippus"
            ),
            conservationStatus = ConservationStatus.VULNERABLE,
            confidencePercent = 97,
            summary = "An iconic migratory brush-footed butterfly celebrated for its vivid amber-orange wings veined in black and its multi-generational 4,000-kilometer continental migration.",
            sizeAndMass = "Wingspan 8.9–10.2 cm • Mass 0.4–0.75 g",
            lifespan = "2–6 weeks (summer) • 6–9 months (migratory generation)",
            activityPattern = "Diurnal",
            physicalTraits = "Tawny-orange wings bordered by a thick black margin speckled with two rows of crisp white spots; males feature a small black scent-scale spot (androconia) on a vein of each hindwing and slightly thinner veins than females.",
            habitatAndRange = "North, Central, and South America, plus established populations in Australia, New Zealand, and Pacific islands. Relies on open meadows, prairies, and gardens rich in milkweed (Asclepias) and nectar blooms.",
            dietAndEcology = "Larvae feed exclusively on milkweed plants, sequestering toxic cardiac glycosides (cardenolides) that make both caterpillars and adults unpalatable to avian predators. Adults sip floral nectar and pollinate wildflowers.",
            behaviorAndReproduction = "Eastern North American migratory monarchs navigate using a sun-compass synchronized to circadian clocks in their antennae, overwintering in dense oyamel fir forests in central Mexico.",
            safetyAndInteraction = "Completely harmless to touch, though handling damages delicate wing scales. Support local populations by planting native milkweed species.",
            funFacts = listOf(
                "It takes up to four successive generations to complete a single round-trip migration.",
                "They taste sweet nectar using chemoreceptors located on their feet (tarsi).",
                "Viceroy butterflies evolved nearly identical patterns through Müllerian mimicry."
            ),
            suggestedQuestions = listOf(
                "How do you tell a Monarch from a Viceroy butterfly?",
                "Why do Monarch caterpillars only eat milkweed?",
                "How do they navigate thousands of miles?"
            )
        ),
        SpeciesInfo(
            id = "atlas_green_sea_turtle",
            commonName = "Green Sea Turtle",
            scientificName = "Chelonia mydas",
            animalClass = AnimalClass.REPTILE,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Reptilia",
                order = "Testudines",
                family = "Cheloniidae",
                genus = "Chelonia",
                species = "C. mydas"
            ),
            conservationStatus = ConservationStatus.ENDANGERED,
            confidencePercent = 94,
            summary = "A large, graceful marine turtle named not for its shell color, but for the greenish hue of its subdermal fat derived from a seagrass and algae diet.",
            sizeAndMass = "Carapace 80–120 cm • Mass 68–190 kg",
            lifespan = "60–80+ years",
            activityPattern = "Diurnal (foraging) & Nocturnal (nesting)",
            physicalTraits = "Smooth, heart-shaped carapace ranging from olive-brown to dark slate with radiating starburst streaks; cream-yellow plastron; a single pair of prefrontal scales between the eyes; and paddle-like flippers with one visible claw.",
            habitatAndRange = "Tropical and subtropical oceans worldwide. Juveniles inhabit open pelagic waters before settling into shallow coastal seagrass beds, coral lagoons, and mangrove bays.",
            dietAndEcology = "Unlike most sea turtles, adults are primarily herbivorous, cropping turtle grass and marine algae. Their grazing maintains healthy, oxygenated seagrass meadows that shelter juvenile fish.",
            behaviorAndReproduction = "Capable of holding breath for up to 5 hours while sleeping wedged under reef ledges. Females return across entire ocean basins to nest on the exact natal beaches where they hatched.",
            safetyAndInteraction = "Protected internationally. Maintain at least 3–5 meters distance while snorkeling, never touch or chase turtles, and avoid bright white flashlights on nesting beaches.",
            funFacts = listOf(
                "Nest sand temperature determines hatchling sex: warmer sand produces more females.",
                "Special salt glands behind their eyes excrete excess ocean salt as thick 'tears'.",
                "They use Earth's geomagnetic signature as a biological GPS to locate home beaches."
            ),
            suggestedQuestions = listOf(
                "Why are they called Green Sea Turtles if their shells look brown?",
                "How long can they stay underwater on one breath?",
                "What threatens their populations most today?"
            )
        ),
        SpeciesInfo(
            id = "atlas_red_eyed_tree_frog",
            commonName = "Red-Eyed Tree Frog",
            scientificName = "Agalychnis callidryas",
            animalClass = AnimalClass.AMPHIBIAN,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Amphibia",
                order = "Anura",
                family = "Phyllomedusidae",
                genus = "Agalychnis",
                species = "A. callidryas"
            ),
            conservationStatus = ConservationStatus.LEAST_CONCERN,
            confidencePercent = 96,
            summary = "An arboreal Neotropical amphibian famous for its startling crimson eyes, neon-green back, cobalt-blue striped flanks, and bright orange adhesive toe pads.",
            sizeAndMass = "Length 5–7.5 cm • Mass 6–15 g",
            lifespan = "5–8 years",
            activityPattern = "Nocturnal",
            physicalTraits = "Vibrant leaf-green dorsum that darkens slightly at night, vivid blue-and-yellow vertical flank bars, bright tangerine feet with suction-cup toe discs, and oversized bulging scarlet eyes with vertical pupils.",
            habitatAndRange = "Lowland tropical rainforests from southern Mexico through Central America to northern Colombia, typically high in canopy foliage overhanging calm forest ponds.",
            dietAndEcology = "Insectivorous ambush predator feeding on crickets, moths, flies, grasshoppers, and occasionally smaller frogs. Serves as crucial prey for snakes, bats, and large spiders.",
            behaviorAndReproduction = "Uses startle coloration: when sleeping with limbs tucked, it looks like a green leaf; if disturbed, flashing scarlet eyes and blue flanks momentarily stun predators ('deimatic display'). Eggs are laid on leaves overhanging water so hatching tadpoles drop directly in.",
            safetyAndInteraction = "Non-toxic to humans (unlike poison dart frogs), though amphibian skin is sensitive to lotions and salts—never handle wild frogs with bare hands.",
            funFacts = listOf(
                "Embryos can hatch days early in seconds if vibrations signal an approaching egg-eating snake.",
                "Its lower eyelids have a gold filigree pattern that camouflages the sleeping frog while letting light pass.",
                "It rarely swims as an adult, spending nearly its entire life climbing rainforest foliage."
            ),
            suggestedQuestions = listOf(
                "Is the Red-Eyed Tree Frog poisonous?",
                "Why do its eyes look so bright red?",
                "How do its tadpoles reach the water?"
            )
        ),
        SpeciesInfo(
            id = "atlas_snowy_owl",
            commonName = "Snowy Owl",
            scientificName = "Bubo scandiacus",
            animalClass = AnimalClass.BIRD,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Aves",
                order = "Strigiformes",
                family = "Strigidae",
                genus = "Bubo",
                species = "B. scandiacus"
            ),
            conservationStatus = ConservationStatus.VULNERABLE,
            confidencePercent = 95,
            summary = "A powerful Arctic tundra raptor clad in dense white plumage, feathered talons, and piercing golden eyes, uniquely adapted to hunt in 24-hour polar daylight.",
            sizeAndMass = "Length 52–71 cm • Wingspan 125–150 cm • Mass 1.6–3 kg",
            lifespan = "9–10 years wild (up to 28 years in care)",
            activityPattern = "Diurnal & Crepuscular",
            physicalTraits = "Dense snow-white plumage (adult males become nearly pure white while females and juveniles retain dark brown barring), rounded head without prominent ear tufts, golden-yellow irises, and thick white feathers covering legs and toes.",
            habitatAndRange = "Circumpolar Arctic tundra across Alaska, northern Canada, Greenland, Scandinavia, and Siberia; irrupts southward in winter to open coastlines, marshes, and prairies.",
            dietAndEcology = "Specialized lemming hunter during Arctic summers—a single owl may consume over 1,600 lemmings per year—and takes waterfowl, hares, and seabirds in winter.",
            behaviorAndReproduction = "Unlike most owls, hunts actively during daytime. Nests directly on slight tundra mounds with unobstructed 360-degree visibility; clutch size tracks local lemming abundance.",
            safetyAndInteraction = "Fiercely protective near nests; keep respectful distance (50+ meters) during winter irruptions so owls conserve vital energy.",
            funFacts = listOf(
                "Feathered toes act like insulated snowshoes and muffle sound on crusted snow.",
                "Its asymmetrical ear openings allow pinpoint acoustic triangulation under snowpack.",
                "Combed serrations on leading wing feathers eliminate aerodynamic turbulence for silent flight."
            ),
            suggestedQuestions = listOf(
                "How do you tell male and female Snowy Owls apart?",
                "Why do Snowy Owls hunt during the day?",
                "What triggers winter irruptions farther south?"
            )
        ),
        SpeciesInfo(
            id = "atlas_bengal_tiger",
            commonName = "Bengal Tiger",
            scientificName = "Panthera tigris tigris",
            animalClass = AnimalClass.MAMMAL,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Mammalia",
                order = "Carnivora",
                family = "Felidae",
                genus = "Panthera",
                species = "P. tigris"
            ),
            conservationStatus = ConservationStatus.ENDANGERED,
            confidencePercent = 98,
            summary = "The apex feline of the Indian subcontinent, distinguished by burnt-orange fur, unique dark vertical stripes, muscular forequarters, and an extraordinary affinity for water.",
            sizeAndMass = "Length 240–310 cm (incl. tail) • Mass 130–260 kg",
            lifespan = "8–10 years wild (up to 20 years in care)",
            activityPattern = "Crepuscular & Nocturnal",
            physicalTraits = "Tawny-orange coat with bold black/dark-brown stripes (unique to every individual like fingerprints), white inner cheeks and belly, pale white 'ocelli' spots behind the ears, and 7.5 cm canine teeth.",
            habitatAndRange = "Primarily India, with smaller populations in Bangladesh (Sundarbans mangroves), Nepal, and Bhutan. Inhabits tropical moist forests, tall alluvial grasslands, and mangrove swamps.",
            dietAndEcology = "Obligate hypercarnivore preying on chital deer, sambar, gaur, wild boar, and water buffalo. As an apex umbrella species, protecting tiger corridors safeguards entire forest watersheds.",
            behaviorAndReproduction = "Solitary and fiercely territorial, marking ranges with scent and tree scrapes. Unlike many cats, tigers swim kilometers across tidal rivers to cool off or hunt.",
            safetyAndInteraction = "Apex predator—view strictly from authorized safari vehicles or elevated hides with trained guides.",
            funFacts = listOf(
                "Its stripe pattern is etched onto its skin underneath the fur.",
                "A tiger's roar can be heard over 3 kilometers away across dense forest.",
                "The white spots on the back of its ears ('predator eyes') deter ambushes from behind."
            ),
            suggestedQuestions = listOf(
                "Are every tiger's stripes really unique?",
                "Why do tigers enjoy swimming so much?",
                "How much meat does a Bengal Tiger eat in one meal?"
            )
        ),
        SpeciesInfo(
            id = "atlas_blue_ringed_octopus",
            commonName = "Greater Blue-Ringed Octopus",
            scientificName = "Hapalochlaena lunulata",
            animalClass = AnimalClass.MOLLUSK,
            taxonomy = TaxonomyTree(
                phylum = "Mollusca",
                className = "Cephalopoda",
                order = "Octopoda",
                family = "Octopodidae",
                genus = "Hapalochlaena",
                species = "H. lunulata"
            ),
            conservationStatus = ConservationStatus.DATA_DEFICIENT,
            confidencePercent = 93,
            summary = "A golf-ball-sized Indo-Pacific cephalopod that flashes electric iridescent blue rings as a warning signal when threatened, carrying potent tetrodotoxin venom.",
            sizeAndMass = "Mantle 4–5 cm • Arm span 10–15 cm • Mass 25–55 g",
            lifespan = "1.5–2 years",
            activityPattern = "Nocturnal & Crepuscular",
            physicalTraits = "Normally mottled sandy-yellow or tan to blend into coral rubble; when agitated, chromatophores retract to reveal 50–60 glowing cobalt-blue rings outlined in dark brown.",
            habitatAndRange = "Shallow coral reefs, tide pools, and rocky crevices across the tropical Indo-West Pacific from northern Australia to Japan and Indonesia.",
            dietAndEcology = "Venomous carnivore hunting small crabs, hermit crabs, shrimp, and occasionally small reef fish.",
            behaviorAndReproduction = "Secretive and docile unless cornered or picked up. Females lay ~50 eggs and brood them tucked inside their arms for months without eating until hatching.",
            safetyAndInteraction = "CRITICAL CAUTION: Carries tetrodotoxin (TTX) produced by symbiotic salivary bacteria—capable of causing painless bites followed by rapid paralysis. Admire strictly from a distance and never touch tide-pool octopuses.",
            funFacts = listOf(
                "The blue rings flash in ~0.3 seconds using muscle-controlled iridophore reflectors.",
                "It has three hearts, blue copper-based blood (hemocyanin), and a donut-shaped brain.",
                "It uses two different venoms: one for immobilizing crabs and one for self-defense."
            ),
            suggestedQuestions = listOf(
                "Why do the blue rings only glow when it's stressed?",
                "How dangerous is a Blue-Ringed Octopus bite?",
                "Where do they hide during the day?"
            )
        ),
        SpeciesInfo(
            id = "atlas_clown_anemonefish",
            commonName = "Ocellaris Clownfish",
            scientificName = "Amphiprion ocellaris",
            animalClass = AnimalClass.FISH,
            taxonomy = TaxonomyTree(
                phylum = "Chordata",
                className = "Actinopterygii",
                order = "Ovalentaria",
                family = "Pomacentridae",
                genus = "Amphiprion",
                species = "A. ocellaris"
            ),
            conservationStatus = ConservationStatus.LEAST_CONCERN,
            confidencePercent = 96,
            summary = "A vibrant coral-reef damselfish famous for its bright orange body crossed by three white bands and its mutualistic partnership with stinging sea anemones.",
            sizeAndMass = "Length 7–11 cm • Mass 20–40 g",
            lifespan = "6–10 years wild (up to 18+ years in care)",
            activityPattern = "Diurnal",
            physicalTraits = "Vivid tangerine-orange body with three crisp white vertical bars edged in thin black lines; rounded caudal and dorsal fins; 11 dorsal spines (helping distinguish it from the 10-spined Percula Clownfish).",
            habitatAndRange = "Warm shallow lagoons and outer coral reefs of the eastern Indian Ocean and western Pacific, from Thailand and Indonesia to northwestern Australia and Japan.",
            dietAndEcology = "Omnivorous planktivore eating copepods, mysid shrimp, and filamentous algae. In return for anemone shelter, it chases away polyp-eating butterflyfish and fertilizes the host.",
            behaviorAndReproduction = "Lives in strict size-based hierarchies led by a single dominant breeding female. All clownfish are born male (protandrous hermaphrodites); if the female dies, the largest male transforms into the new female.",
            safetyAndInteraction = "Completely harmless to snorkelers and divers; avoid touching host anemones to preserve the fish's protective mucus coat.",
            funFacts = listOf(
                "A specialized sugar-based mucus layer lacks the chemical trigger that fires anemone nematocyst stings.",
                "Every individual in a social group can change sex once—from male to female—if the matriarch disappears.",
                "Males handle most of the egg-fanning and nest-cleaning duties."
            ),
            suggestedQuestions = listOf(
                "How do Clownfish avoid getting stung by sea anemones?",
                "How can you tell Ocellaris from Percula Clownfish?",
                "Why do they live in small family groups?"
            )
        ),
        SpeciesInfo(
            id = "atlas_peacock_spider",
            commonName = "Peacock Jumping Spider",
            scientificName = "Maratus volans",
            animalClass = AnimalClass.ARTHROPOD,
            taxonomy = TaxonomyTree(
                phylum = "Arthropoda",
                className = "Arachnida",
                order = "Araneae",
                family = "Salticidae",
                genus = "Maratus",
                species = "M. volans"
            ),
            conservationStatus = ConservationStatus.DATA_DEFICIENT,
            confidencePercent = 94,
            summary = "A minuscule Australian jumping spider whose males unfurl an iridescent turquoise, crimson, and gold abdominal fan while performing choreographed courtship dances.",
            sizeAndMass = "Body length 4–5 mm • Mass < 0.02 g",
            lifespan = "1 year",
            activityPattern = "Diurnal",
            physicalTraits = "Four pairs of eyes including two giant forward-facing principal eyes with acute color vision; females are mottled brown for camouflage while males carry iridescent structural-color scales on expandable abdominal flaps.",
            habitatAndRange = "Coastal heathlands, woodlands, and dry leaf litter in eastern Australia (New South Wales and Queensland).",
            dietAndEcology = "Visual stalk-and-pounce predator feeding on tiny flies, aphids, and springtails. Does not build capture webs, relying instead on hydraulic jumping power up to 40 times its body length.",
            behaviorAndReproduction = "During courtship, males raise their third pair of legs, vibrate their abdomen to produce substrate drumbeats, and fan out their colorful flaps in rhythmic side-to-side semaphore dances.",
            safetyAndInteraction = "Harmless to humans—its tiny fangs cannot pierce human skin and it poses zero medical risk.",
            funFacts = listOf(
                "Before every leap, it anchors a silk safety dragline in case it misses its target.",
                "Its front pair of eyes can focus independently like miniature telephoto lenses.",
                "Its iridescent blue scales contain microscopic diffraction gratings that stay vibrant at any angle."
            ),
            suggestedQuestions = listOf(
                "How high can a Peacock Jumping Spider leap?",
                "Why do only the males have bright colors?",
                "Are they dangerous to humans?"
            )
        )
    )
}
