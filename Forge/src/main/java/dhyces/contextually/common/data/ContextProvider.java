package dhyces.contextually.common.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ContextProvider implements DataProvider {

    @Nullable LanguageProvider languageProvider;
    PackOutput.PathProvider pathProvider;
    ExistingFileHelper fileHelper;
    String modid;
    final String contextPath = "contexts";
    final Map<ResourceLocation, IGeneratedContext> data = Maps.newHashMap();

    public ContextProvider(@Nullable LanguageProvider languageProvider, @NotNull PackOutput output, @NotNull ExistingFileHelper fileHelper, @NotNull String modid, @Nullable String folder) {
        this.languageProvider = languageProvider;
        Preconditions.checkNotNull(output);
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, folder == null ? contextPath : contextPath + '/' + folder);
        Preconditions.checkNotNull(fileHelper);
        this.fileHelper = fileHelper;
        Preconditions.checkNotNull(modid);
        this.modid = modid;
    }

    protected abstract void addContexts();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addContexts();
        return null;
    }

    @Override
    public String getName() {
        return "Contextually Context Provider";
    }
}
