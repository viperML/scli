{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    sbt = {
      url = "github:zaninime/sbt-derivation/master";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      sbt,
    }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      packages.${system}.default =
        (sbt.mkSbtDerivation.${system}).withOverrides ({ stdenv = pkgs.llvmPackages.stdenv; })
          {
            pname = "scli";
            version = "0.1.0";
            src = ./.;
            depsSha256 = "sha256-kMKbtWpUhC3Em+skS2q7LblugLf8JwHs1araUr5bkJ8=";
            env.NIX_CFLAGS_COMPILE = "-Wno-unused-command-line-argument";
            hardeningDisable = [ "fortify" ];
            buildPhase = ''
              sbt compile
            '';
            installPhase = ''
              sbt 'show stage'
              mkdir -p $out/bin
              cp target/scli $out/bin/
            '';
            nativeBuildInputs = [
              pkgs.which
            ];
          };

      devShells.${system}.myshell =
        with pkgs;
        mkShellNoCC {
          packages = [
            # -
            openjdk
          ];
        };
    };
}
