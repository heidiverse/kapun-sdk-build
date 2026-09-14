import ch.ubique.uniffi.plugin.dsl.CargoExtension
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.configure

plugins {
	// Kotlin & KMP plugins
	alias(libs.plugins.kotlin.multiplatform) apply false
	alias(libs.plugins.kotlin.parcelize) apply false
	alias(libs.plugins.kotlin.serialization) apply false
	alias(libs.plugins.kotlin.atomicfu) apply false
	alias(libs.plugins.compose.multiplatform) apply false
	alias(libs.plugins.sqldelight) apply false
	alias(libs.plugins.jetbrains.kotlin.jvm) apply false

	// Android specific plugins
	alias(libs.plugins.android.kotlin.multiplatform.library) apply false
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.ksp) apply false
	alias(libs.plugins.ktorfit) apply false

	// iOS specific plugins
	alias(libs.plugins.skie) apply false

	// Rust plugins
	alias(libs.plugins.uniffi.plugin) apply false

	// Library publishing plugins
	alias(libs.plugins.vanniktech.publish) apply false
}

val isolatedCargoTarget = providers.gradleProperty("isolatedCargoTarget")
    .map(String::toBoolean)
    .orElse(false)

subprojects {
	pluginManager.withPlugin("ch.ubique.uniffi.plugin") {
		extensions.configure<CargoExtension> {
			// Developers use one visible shared cache to avoid duplicate Rust artifacts. CI can opt
			// into one target directory per module so Gradle/Cargo builds run concurrently; sccache
			// still deduplicates compiler results across those directories.
			targetDirectory.set(
				if (isolatedCargoTarget.get()) {
					rootProject.layout.projectDirectory.dir("cargo-build/${project.name}")
				} else {
					rootProject.layout.projectDirectory.dir("cargo-build")
				}
			)
		}
	}
}

// `clean` intentionally preserves the shared Rust cache. Use this task when a completely fresh
// Cargo build is needed or when reclaiming disk space.
tasks.register<Delete>("cleanCargoCache") {
	group = "build"
	description = "Delete the shared Cargo target directory"
	delete(layout.projectDirectory.dir("cargo-build"))
}

allprojects {
	group = "org.kapunsdk"
	version = getProjectVersion()

}

private fun getProjectVersion(): String {
	val versionFromGradleProperties = runCatching { property("ARTIFACT_VERSION").toString() }.getOrNull()
	val versionFromWorkflow = runCatching { property("githubRefName").toString().removePrefix("v") }.getOrNull()
	return versionFromWorkflow ?: versionFromGradleProperties ?: "untagged"
}
