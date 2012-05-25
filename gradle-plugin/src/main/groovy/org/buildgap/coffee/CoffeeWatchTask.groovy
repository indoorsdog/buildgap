package org.buildgap.coffee

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.monitor.FileAlterationListener
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTree
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.TaskAction

class CoffeeWatchTask extends DefaultTask {



	def File getCoffeeOutputDirectory(File coffeeInputBaseDirectory, File coffeeOutputBaseDirectory, File coffeeFile) {
		List inputDirectoryPathSegments = RelativePath.parse(false, coffeeInputBaseDirectory.absolutePath).segments
		List inputFilePathSegments = RelativePath.parse(true, coffeeFile.absolutePath).segments
		inputFilePathSegments.pop() // get rid of file name at end of path

		for(def pair : [inputDirectoryPathSegments, inputFilePathSegments].transpose()) {
			if (pair[0] == pair[1]) {
				inputFilePathSegments.remove(0)
			} else {
				break
			}
		}

		File outputDirectory = new RelativePath(false, inputFilePathSegments as String[]).getFile(coffeeOutputBaseDirectory)
		return outputDirectory
	}

	def File getJavaScriptFile(File coffeeInputBaseDirectory, File coffeeOutputBaseDirectory, File coffeeFile) {
		File coffeeOutputDirectory = getCoffeeOutputDirectory(coffeeInputBaseDirectory, coffeeOutputBaseDirectory, coffeeFile)
		String fileBaseName = FilenameUtils.getBaseName(coffeeFile.absolutePath)
		File javaScriptFile = new File(coffeeOutputDirectory, fileBaseName + FilenameUtils.EXTENSION_SEPARATOR_STR + 'js')
		return javaScriptFile
	}

	def prepareCoffeeOutputBaseDirectory() {
		File coffeeOutputBaseDirectory = project.file(new File(project.getBuildDir(), 'watchOutput'))	
		if (coffeeOutputBaseDirectory.exists()) {
			FileUtils.cleanDirectory(coffeeOutputBaseDirectory)
		} else {
			FileUtils.forceMkdir(coffeeOutputBaseDirectory)
		}
	}

	@TaskAction
	def watch() {
		prepareCoffeeOutputBaseDirectory()
		File coffeeInputBaseDirectory = project.file('src/main/coffeescript/')
		/* todo: put in to a property */
		File coffeeOutputBaseDirectory = project.file(new File(project.getBuildDir(), 'watchOutput'))	
		ConfigurableFileTree coffeeFileTree = project.fileTree(dir: coffeeInputBaseDirectory, includes: ['**/*.coffee'])

		FileAlterationListener fileAlterationListener = new FileAlterationListener() {
			void onStart(FileAlterationObserver fileAlterationObserver) {
			}
			void onStop(FileAlterationObserver fileAlterationObserver) {
			}
			void onDirectoryChange(File directory) {
				println 'onDirectoryChange: ' + directory
			}
			void onDirectoryCreate(File directory) {
				println 'onDirectoryCreate: ' + directory
			}
			void onDirectoryDelete(File directory) {
				println 'onDirectoryDelete: ' + directory
			}
			void onFileChange(File file) {
				if (coffeeFileTree.contains(file)) {
					File coffeeOutputDirectory = getCoffeeOutputDirectory(coffeeInputBaseDirectory, coffeeOutputBaseDirectory, file)
					Process coffeeProcess = ['coffee', '--compile', '--output', coffeeOutputDirectory.absolutePath, file.absolutePath].execute()
					coffeeProcess.waitFor()
					println 'CoffeeScript file compiled: ' + file.absolutePath
				}
			}
			void onFileCreate(File file) {
				onFileChange(file)
			}
			void onFileDelete(File file) {
				FileTree outputTree = project.fileTree(dir: coffeeOutputBaseDirectory, includes: ['**/*.js'])
				File javaScriptFile = getJavaScriptFile(coffeeInputBaseDirectory, coffeeOutputBaseDirectory, file)
				if (outputTree.contains(javaScriptFile)) {
					javaScriptFile.delete()
					println 'JavaScript file deleted: ' + file.absolutePath
				}
				File parentDirectory = file.getParentFile()
				println parentDirectory.listFiles()
				if (!parentDirectory.listFiles().any{ coffeeFileTree.contains(it) }) {
					FileUtils.deleteDirectory(parentDirectory)
				}
			}
		}

		FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(coffeeInputBaseDirectory)
		fileAlterationObserver.addListener(fileAlterationListener)
		FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(1000, fileAlterationObserver)
		fileAlterationMonitor.start()

		println 'Press <Enter> to stop watching.'
		System.in.read()
	}

}