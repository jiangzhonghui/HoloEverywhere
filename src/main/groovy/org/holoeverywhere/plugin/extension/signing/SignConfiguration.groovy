package org.holoeverywhere.plugin.extension.signing

import com.android.build.gradle.internal.dsl.SigningConfigDsl
import com.android.builder.model.SigningConfig
import org.gradle.api.Project
import org.gradle.util.Configurable
import org.gradle.util.ConfigureUtil
import org.holoeverywhere.plugin.extension.ExProperties

class SignConfiguration implements Configurable<SignConfiguration> {
    SignConfiguration(Project project) {
        this.project = project
    }

    private final Project project
    def String storeFile
    def String keyAlias
    def String storePassword
    def String keyPassword

    @Override
    SignConfiguration configure(Closure closure) {
        ConfigureUtil.configure(closure, this, false)
    }

    public boolean valid() {
        return storeFile && keyAlias && storePassword && keyPassword && new File(storeFile).exists()
    }

    public SignConfiguration key(String key) {
        ExProperties props = new ExProperties(project)
        storeFile = props.property(key, 'StoreFile')
        keyAlias = props.property(key, 'KeyAlias')
        storePassword = props.property(key, 'StorePassword')
        keyPassword = props.property(key, 'KeyPassword')
        return this
    }

    def SigningConfig obtainConfig(String name) {
        SigningConfig config = new SigningConfigDsl(name)
        config.storeFile = new File(storeFile)
        config.keyAlias = keyAlias
        config.storePassword = storePassword
        config.keyPassword = keyPassword
        return config
    }

    def SigningConfig obtainDebugConfig(String name = 'debug') {
        SigningConfig config = new SigningConfigDsl(name)
        config.initDebug()
        return config
    }

    def SigningConfig obtainMaybeDebugConfig(String name = 'debug') {
        return valid() ? obtainConfig(name) : obtainDebugConfig(name)
    }
}